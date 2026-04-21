using System;
using System.Collections.Generic;
using System.Drawing;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using System.Windows.Forms;
using static System.Net.WebRequestMethods;


namespace IMPULS_Desktop
{
    public partial class Candidats : Form
    {
        private readonly HttpClient client = new HttpClient();
        private List<Candidatos> candidats = new List<Candidatos>();

        private int ofertaId = 1;
        private string apiUrl =>

    $"http://0bb0dfb7-9b4c-40bc-a0be.5b8c35470a40.bastion.elmeuescriptori.cat/offers/{ofertaId}/applicants?sessionId={PantallaPrincipal.SessionId}";

        public Candidats(int ofertaId)
        {
            InitializeComponent();
            this.ofertaId = ofertaId;
        }

        private async void Candidats_Load(object sender, EventArgs e)
        {
            await CargarCandidats();


            dataGridView1.CellEndEdit += dataGridView1_CellEndEdit;
            dataGridView1.CellFormatting += dataGridView1_CellFormatting;
            dataGridView1.DataSource = candidats;

            AjustarAlturaGrid();

        }
        private void AjustarAlturaGrid()
        {
            int height = dataGridView1.ColumnHeadersHeight;

            foreach (DataGridViewRow row in dataGridView1.Rows)
            {
                height += row.Height;
            }

            dataGridView1.Height = height;
        }
        private void dataGridView1_CellFormatting(object sender, DataGridViewCellFormattingEventArgs e)
        {
            if (dataGridView1.Columns[e.ColumnIndex].Name == "status")
            {
                var valor = e.Value?.ToString();

                if (valor == "ACCEPTED")
                {
                    e.CellStyle.BackColor = Color.LightGreen;
                }
                else if (valor == "REJECTED")
                {
                    e.CellStyle.BackColor = Color.LightCoral;
                }
                else if (valor == "PENDING")
                {
                    e.CellStyle.BackColor = Color.LightYellow;
                }
            }
        }
        private async System.Threading.Tasks.Task CargarCandidats()
        {
            try
            {
                var response = await client.GetAsync(apiUrl);
                response.EnsureSuccessStatusCode();

                var json = await response.Content.ReadAsStringAsync();

                candidats = JsonSerializer.Deserialize<List<Candidatos>>(json,
                    new JsonSerializerOptions { PropertyNameCaseInsensitive = true });

                dataGridView1.DataSource = null;
                dataGridView1.DataSource = candidats;
            }
            catch (HttpRequestException)
            {
                MessageBox.Show("No es pot conectar amb el servidor");
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error: " + ex.Message);
            }
        }


        private async void dataGridView1_CellEndEdit(object sender, DataGridViewCellEventArgs e)
        {
            try
            {
                var c = (Candidatos)dataGridView1.Rows[e.RowIndex].DataBoundItem;

                var json = JsonSerializer.Serialize(c);
                var content = new StringContent(json, Encoding.UTF8, "application/json");


            }
            catch (HttpRequestException)
            {
                MessageBox.Show("Error de conexión quan guardem");
                await CargarCandidats();
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error quan actualitzem: " + ex.Message);
                await CargarCandidats();
            }
        }


        private async void btnTriarCandidat_Click(object sender, EventArgs e)
        {
            if (dataGridView1.CurrentRow == null)
            {
                MessageBox.Show("Tria un candidat");
                return;
            }

            var c = (Candidatos)dataGridView1.CurrentRow.DataBoundItem;

            var confirm = MessageBox.Show(
                "Acceptar candidatura de " + c.Name + "?",
                "Confirmar",
                MessageBoxButtons.YesNo);

            if (confirm != DialogResult.Yes)
                return;

            try
            {
                await CambiarEstado(c.applicationId, "ACCEPTED");
                MessageBox.Show("Candidatura acceptada");

                await CargarCandidats();
            }
            catch (HttpRequestException ex)
            {
                MessageBox.Show("Error HTTP: " + ex.Message);
                MessageBox.Show("Error de connexió");
            }
        }
        private async Task CambiarEstado(int applicationId, string nuevoEstado)
        {
            var json = JsonSerializer.Serialize(new { status = nuevoEstado });
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            var request = new HttpRequestMessage(
                new HttpMethod("PATCH"),
                $"http://0bb0dfb7-9b4c-40bc-a0be.5b8c35470a40.bastion.elmeuescriptori.cat/applications/{applicationId}?sessionId={PantallaPrincipal.SessionId}"
            )
            {
                Content = content
            };

            var response = await client.SendAsync(request);
            response.EnsureSuccessStatusCode();
        }
        private async void btnEliminarCandidat_Click(object sender, EventArgs e)
        {
            if (dataGridView1.CurrentRow == null)
            {
                MessageBox.Show("Tria un candidat");
                return;
            }

            var c = (Candidatos)dataGridView1.CurrentRow.DataBoundItem;

            var confirm = MessageBox.Show(
                "Rebutjar candidatura de " + c.Name + "?",
                "Confirmar",
                MessageBoxButtons.YesNo);

            if (confirm != DialogResult.Yes)
                return;

            try
            {
                await CambiarEstado(c.applicationId, "REJECTED");
              //  await CambiarEstado(c.IdCandidatura, "Rechazada");

                MessageBox.Show("Candidatura rebutjada");

                await CargarCandidats();
            }
            catch (HttpRequestException)
            {
                MessageBox.Show("Error de connexió");
            }
        }

        private void btnTancar_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        private void btnTornar_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }
    }


}
