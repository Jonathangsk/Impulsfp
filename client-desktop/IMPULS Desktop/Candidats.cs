using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Windows.Forms;

namespace IMPULS_Desktop
{
    public partial class Candidats : Form
    {
        private readonly HttpClient client = new HttpClient();
        private List<Candidat> candidats = new List<Candidat>();

//        private string apiUrl = "https://tu-servidor/api/candidats";
        private int ofertaId = 1;
        private string apiUrl => $"https://tu-servidor/api/candidats/oferta/{ofertaId}";

        public Candidats(int ofertaId)
        {
            InitializeComponent();
            this.ofertaId = ofertaId;
        }

        private async void Candidats_Load(object sender, EventArgs e)
        {
            await CargarCandidats();

            
            dataGridView1.CellEndEdit += dataGridView1_CellEndEdit;
        }

      
        private async System.Threading.Tasks.Task CargarCandidats()
        {
            try
            {
                var response = await client.GetAsync(apiUrl);
                response.EnsureSuccessStatusCode();

                var json = await response.Content.ReadAsStringAsync();

                candidats = JsonSerializer.Deserialize<List<Candidat>>(json,
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
                var c = (Candidat)dataGridView1.Rows[e.RowIndex].DataBoundItem;

                var json = JsonSerializer.Serialize(c);
                var content = new StringContent(json, Encoding.UTF8, "application/json");

                var response = await client.PutAsync($"{apiUrl}/{c.Id}", content);
                response.EnsureSuccessStatusCode();
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

            var c = (Candidat)dataGridView1.CurrentRow.DataBoundItem;

            try
            {
                // 1. Marquem candidat com seleccionat
                await client.PutAsync(
                    $"http://localhost:8080/candidats/{c.Id}/select",
                    null
                );

                // 2. Marquem oferta com expired
                await client.PutAsync(
                    $"http://localhost:8080/offers/{ofertaId}/expire",
                    null
                );

                MessageBox.Show("Candidat seleccionat i oferta tancada");

                await CargarCandidats();
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error: " + ex.Message);
            }
        }

        private async void btnEliminarCandidat_Click(object sender, EventArgs e)
        {
            if (dataGridView1.CurrentRow == null)
            {
                MessageBox.Show("Tria un candidat");
                return;
            }

            var c = (Candidat)dataGridView1.CurrentRow.DataBoundItem;

            var confirm = MessageBox.Show(
                "Segur que vols eliminar a " + c.Nom + "?",
                "Confirmar",
                MessageBoxButtons.YesNo);

            if (confirm != DialogResult.Yes)
                return;

            try
            {
                var response = await client.DeleteAsync($"{apiUrl}/{c.Id}");
                response.EnsureSuccessStatusCode();

                await CargarCandidats();
            }
            catch (HttpRequestException)
            {
                MessageBox.Show("Error de conexió");
            }
        }

        private void btnTancar_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void btnTornar_Click(object sender, EventArgs e)
        {
            this.Close();
        }


    }


    public class Candidat
    {
        public int Id { get; set; }
        public string Nom { get; set; }
    }
}