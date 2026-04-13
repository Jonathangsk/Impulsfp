using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Net.Http;
using System.Text.Json;

namespace IMPULS_Desktop
{
    public partial class OfertesDeTreball : Form
    {
        public OfertesDeTreball()
        {
            InitializeComponent();
        }

        private async void OfertesDeTreball_Load(object sender, EventArgs e)
        {
            var offers = await GetOffers();

            dataGridView1.DataSource = offers;

            dataGridView1.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            dataGridView1.MultiSelect = false;
        }

        private async Task<List<Oferta>> GetOffers()
        {
         using (HttpClient client = new HttpClient())
            {
                try
                {
                    var json = await client.GetStringAsync("http://localhost:8080/offers");

                    return JsonSerializer.Deserialize<List<Oferta>>(
                        json,
                        new JsonSerializerOptions
                        {
                            PropertyNameCaseInsensitive = true
                        }
                    ) ?? new List<Oferta>();
                }
                catch (HttpRequestException ex)
                {
                    MessageBox.Show("No se pudo conectar al servidor (posible 404 o servidor caído).");
                }
                catch (TaskCanceledException)
                {
                    MessageBox.Show("La petición tardó demasiado (timeout).");
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Error inesperado: " + ex.Message);
                }

                return new List<Oferta>();
            }
        }



       //Editar

        private void btnEditar_Click(object sender, EventArgs e)
        {
            if (dataGridView1.CurrentRow == null) return;

            var oferta = (Oferta)dataGridView1.CurrentRow.DataBoundItem;

            MessageBox.Show("Editar: " + oferta.Title);
        }

        //Eliminar
        private async void btnEliminar_Click(object sender, EventArgs e)
        {
            if (dataGridView1.CurrentRow == null) return;

            var oferta = (Oferta)dataGridView1.CurrentRow.DataBoundItem;

            var confirm = MessageBox.Show("¿Segur que vols eliminar?",
                "Confirmar", MessageBoxButtons.YesNo);

            if (confirm == DialogResult.Yes)
            {
                using (HttpClient client = new HttpClient())
                {
                    await client.DeleteAsync($"http://localhost:8080/offers/{oferta.Id}");
                }

                dataGridView1.DataSource = await GetOffers();
            }
        }

        //Tancar
        private void btnTancar_Click(object sender, EventArgs e)
        {
            Application.Exit();
            
        }

        private void btnCandidats_Click(object sender, EventArgs e)
        {
            if (dataGridView1.CurrentRow == null)
            {
                MessageBox.Show("Selecciona una oferta");
                return;
            }

            var oferta = (Oferta)dataGridView1.CurrentRow.DataBoundItem;

            var form = new Candidats(oferta.Id);
            form.Show();
        }

        private void btnTornar_Click(object sender, EventArgs e)
        {
            this.Close();
        }
    }
    }
