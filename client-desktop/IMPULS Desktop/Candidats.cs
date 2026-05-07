using System;
using System.Collections.Generic;
using System.Drawing;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace IMPULS_Desktop
{
    /// <summary>
    /// Formulari que mostra els candidats d’una oferta.
    /// Permet acceptar o rebutjar candidatures.
    /// </summary>
    public partial class Candidats : Form
    {
        private readonly HttpClient client = new HttpClient();
        private List<Candidatos> candidats = new List<Candidatos>();

        private int ofertaId = 1;
        private string apiUrl =>

    $"http://0bb0dfb7-9b4c-40bc-a0be.5b8c35470a40.bastion.elmeuescriptori.cat/offers/{ofertaId}/applicants?sessionId={PantallaPrincipal.SessionId}";

        /// <summary>
        /// Constructor del formulari
        /// </summary>
        public Candidats(int ofertaId)
        {
            InitializeComponent();
            this.ofertaId = ofertaId;
        }

        /// <summary>
        /// Esdeveniment Load del formulari.
        /// Configura el DataGridView i carrega els candidats.
        /// </summary>
        private async void Candidats_Load(object sender, EventArgs e)
        {
            // Configuració del DataGridView
            dataGridView1.ReadOnly = true;
            dataGridView1.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            dataGridView1.MultiSelect = false;

            // Event per colorear estats
            dataGridView1.CellFormatting += dataGridView1_CellFormatting;

            // Carregar dades
            await CarregarCandidats();

            // Ajustar tamany despres de carregar
            AjustarAlturaGrid();
        }
        /// <summary>
        /// Ajusta l’alçada del DataGridView segons les files
        /// </summary>
        private void AjustarAlturaGrid()
        {
            int height = dataGridView1.ColumnHeadersHeight;

            foreach (DataGridViewRow row in dataGridView1.Rows)
            {
                height += row.Height;
            }

            dataGridView1.Height = height;
        }
        /// <summary>
        /// Pinta les files segons l’estat del candidat
        /// </summary>
        private void dataGridView1_CellFormatting(object sender, DataGridViewCellFormattingEventArgs e)
        {
            //var columnName = dataGridView1.Columns[e.ColumnIndex].Name;
            //var valor = e.Value?.ToString();
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
            /*else if (columnName == "testResult")
            {
                if (valor == "PASSED")
                {
                    e.CellStyle.BackColor = Color.LightGreen;
                }
                else if (valor == "FAILED")
                {
                    e.CellStyle.BackColor = Color.LightCoral;
                }
            }*/
        }
        /// <summary>
        /// Carrega els candidats des de l’API
        /// </summary>
        private async System.Threading.Tasks.Task CarregarCandidats()
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

        /// <summary>
        /// Accepta un candidat seleccionat
        /// </summary>
        private async void btnTriarCandidat_Click(object sender, EventArgs e)
        {
            if (dataGridView1.CurrentRow == null)
            {
                MessageBox.Show("Tria un candidat");
                return;
            }

            var c = (Candidatos)dataGridView1.CurrentRow.DataBoundItem;

            var confirm = MessageBox.Show(
                "Acceptar candidatura de " + c.name + "?",
                "Confirmar",
                MessageBoxButtons.YesNo);

            if (confirm != DialogResult.Yes)
                return;

            try
            {
                await CambiarEstat(c.applicationId, "ACCEPTED");
                MessageBox.Show("Candidatura acceptada");

                await CarregarCandidats();
            }
            catch (HttpRequestException ex)
            {
                MessageBox.Show("Error HTTP: " + ex.Message);
                MessageBox.Show("Error de connexió");
            }
        }

        /// <summary>
        /// Canvia l’estat d’una candidatura (PATCH a l’API)
        /// </summary>
        private async Task CambiarEstat(int applicationId, string nuevoEstado)
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
        /// <summary>
        /// Rebutja un candidat seleccionat
        /// </summary>
        private async void btnEliminarCandidat_Click(object sender, EventArgs e)
        {
            if (dataGridView1.CurrentRow == null)
            {
                MessageBox.Show("Tria un candidat");
                return;
            }

            var c = (Candidatos)dataGridView1.CurrentRow.DataBoundItem;

            var confirm = MessageBox.Show(
                "Rebutjar candidatura de " + c.name + "?",
                "Confirmar",
                MessageBoxButtons.YesNo);

            if (confirm != DialogResult.Yes)
                return;

            try
            {
                await CambiarEstat(c.applicationId, "REJECTED");
              
                MessageBox.Show("Candidatura rebutjada");

                await CarregarCandidats();
            }
            catch (HttpRequestException)
            {
                MessageBox.Show("Error de connexió");
            }
        }

        /// <summary>
        /// Tanca l’aplicació
        /// </summary>
        private void btnTancar_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        /// <summary>
        /// Tanca el formulari actual
        /// </summary>
        private void btnTornar_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }
    }
}
