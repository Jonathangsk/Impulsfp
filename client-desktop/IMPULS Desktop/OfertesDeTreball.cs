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
        private PantallaEmpresa _pantallaEmpresa;
        private readonly HttpClient client = new HttpClient();
        public OfertesDeTreball(PantallaEmpresa pantallaEmpresa)
        {
            InitializeComponent();
            _pantallaEmpresa = pantallaEmpresa;
            this.FormClosing += OfertesDeTreball_FormClosing;
        }

        private async void OfertesDeTreball_Load(object sender, EventArgs e)
        {
            var offers = await GetOffers();

            dataGridView1.DataSource = offers;

            dataGridView1.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            dataGridView1.MultiSelect = false;

            
            if (dataGridView1.Columns.Contains("Skills"))
                dataGridView1.Columns["Skills"].Visible = false;

            if (dataGridView1.Columns.Contains("Applicants"))
                dataGridView1.Columns["Applicants"].Visible = false;

            if (dataGridView1.Columns.Contains("Cicle"))
                dataGridView1.Columns["Cicle"].Visible = false;

            if (dataGridView1.Columns.Contains("Observacions"))
                dataGridView1.Columns["Observacions"].Visible = false;
        }

        private async Task<List<Oferta>> GetOffers()
        {
            using (HttpClient client = new HttpClient())
            {
                try
                {
                    string url =
                        $"http://0bb0dfb7-9b4c-40bc-a0be.5b8c35470a40.bastion.elmeuescriptori.cat:80/offers/my?sessionId={PantallaPrincipal.SessionId}";

                    var json = await client.GetStringAsync(url);

                    Console.WriteLine(json);

                    var options = new JsonSerializerOptions
                    {
                        PropertyNameCaseInsensitive = true
                    };

                    var offers = JsonSerializer.Deserialize<List<Oferta>>(json, options);

                 
                    foreach (var o in offers)
                    {
                        if (o.skills == null)
                            o.skills = new List<string>();
                    }

                    return offers ?? new List<Oferta>();
                }
                catch (HttpRequestException)
                {
                    MessageBox.Show("No se pudo conectar al servidor.");
                }
                catch (TaskCanceledException)
                {
                    MessageBox.Show("Timeout en la petición.");
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Error inesperado: " + ex.Message);
                }

                return new List<Oferta>();
            }
        }
        private async void dataGridView1_CellEndEdit(object sender, DataGridViewCellEventArgs e)
        {
            try
            {
                var oferta = (Oferta)dataGridView1.Rows[e.RowIndex].DataBoundItem;

                using (HttpClient client = new HttpClient())
                {

                    string url =
                      $"http://0bb0dfb7-9b4c-40bc-a0be.5b8c35470a40.bastion.elmeuescriptori.cat:80/offers/my?sessionId={PantallaPrincipal.SessionId}";

                    var json = JsonSerializer.Serialize(oferta);
                    var content = new StringContent(json, System.Text.Encoding.UTF8, "application/json");

                    var response = await client.PutAsync(url, content);

                    if (!response.IsSuccessStatusCode)
                    {
                        MessageBox.Show("Error guardando cambios");
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error: " + ex.Message);
            }
        }
       
        private void btnEditar_Click(object sender, EventArgs e)
        {
            if (dataGridView1.CurrentRow == null) return;

            var oferta = (Oferta)dataGridView1.CurrentRow.DataBoundItem;

          
            MessageBox.Show("Editar oferta ID: " + oferta.Id);
        }
        private decimal ParseDecimalSafe(object value)
        {
            if (value == null) return 0;

            decimal result;

            decimal.TryParse(
                value.ToString().Replace(",", "."),
                System.Globalization.NumberStyles.Any,
                System.Globalization.CultureInfo.InvariantCulture,
                out result
            );

            return result;
        }
     
        private async void btnEliminar_Click(object sender, EventArgs e)
        {
            if (dataGridView1.CurrentRow == null)
            {
                MessageBox.Show("Selecciona una oferta");
                return;
            }

            var oferta = (Oferta)dataGridView1.CurrentRow.DataBoundItem;

            var confirm = MessageBox.Show(
                "Segur que vols eliminar aquesta oferta?",
                "Confirmar",
                MessageBoxButtons.YesNo,
                MessageBoxIcon.Warning);

            if (confirm != DialogResult.Yes)
                return;

            try
            {
                var response = await client.DeleteAsync(
                    $"http://0bb0dfb7-9b4c-40bc-a0be.5b8c35470a40.bastion.elmeuescriptori.cat/offers/{oferta.Id}?sessionId={PantallaPrincipal.SessionId}"
                );

                response.EnsureSuccessStatusCode();

                MessageBox.Show("Oferta eliminada correctament");

                dataGridView1.DataSource = await GetOffers();
            }
            catch (HttpRequestException)
            {
                MessageBox.Show("Error de connexió amb el servidor");
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error inesperat: " + ex.Message);
            }
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

     
        private void btnTancar_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        
        private void btnTornar_Click(object sender, EventArgs e)
        {
            _pantallaEmpresa.Show();
            this.Close();
        }

        private void OfertesDeTreball_FormClosing(object sender, FormClosingEventArgs e)
        {
            _pantallaEmpresa.Show();
        }
    }
}