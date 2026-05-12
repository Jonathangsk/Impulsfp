using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Net.Http;
using System.Text.Json;
using System.Text;

namespace IMPULS_Desktop
{
    /// <summary>
    /// Formulari que mostra les ofertes de treball d’una empresa.
    /// </summary>
    public partial class OfertesDeTreball : Form
    {
        private PantallaEmpresa _pantallaEmpresa;

        // CLIENT HTTPS
        private static readonly HttpClient client;

        static OfertesDeTreball()
        {
            var handler = new HttpClientHandler();

            handler.ServerCertificateCustomValidationCallback =
                (message, cert, chain, errors) => true;

            client = new HttpClient(handler);

            client.Timeout = TimeSpan.FromSeconds(30);
        }
        /// <summary>
        /// Constructor del formulari.
        /// Assigna pantalla pare i events inicials.
        /// </summary>
        public OfertesDeTreball(PantallaEmpresa pantallaEmpresa)
        {
            InitializeComponent();

            _pantallaEmpresa = pantallaEmpresa;

            this.FormClosing += OfertesDeTreball_FormClosing;

            dataGridView1.CellEndEdit += dataGridView1_CellEndEdit;
        }
        /// <summary>
        /// Carrega les ofertes quan s’obre el formulari.
        /// </summary>
        private async void OfertesDeTreball_Load(object sender, EventArgs e)
        {
            var offers = await GetOffers();

            dataGridView1.DataSource = offers;

            dataGridView1.SelectionMode =
                DataGridViewSelectionMode.FullRowSelect;

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
        /// <summary>
        /// Obté les ofertes de l’API.
        /// </summary>
        private async Task<List<Oferta>> GetOffers()
        {
            try
            {
                string baseUrl =
                    PantallaPrincipal.apiBase.Replace("/auth", "");

                string url =
                    $"{baseUrl}/offers/my?sessionId={PantallaPrincipal.SessionId}";

                var json = await client.GetStringAsync(url);

                var options = new JsonSerializerOptions
                {
                    PropertyNameCaseInsensitive = true
                };

                var offers =
                    JsonSerializer.Deserialize<List<Oferta>>(json, options);

                if (offers != null)
                {
                    foreach (var o in offers)
                    {
                        if (o.Skills == null)
                            o.Skills = new List<string>();
                    }
                }

                return offers ?? new List<Oferta>();
            }
            catch (HttpRequestException ex)
            {
                MessageBox.Show("Error HTTP: " + ex.Message);
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
        /// <summary>
        /// Actualitza una oferta quan s’edita una cel·la.
        /// </summary>
        private async void dataGridView1_CellEndEdit(
            object sender,
            DataGridViewCellEventArgs e)
        {
            try
            {
                if (e.RowIndex < 0) return;

                var fila = dataGridView1.Rows[e.RowIndex];

                int id = Convert.ToInt32(fila.Cells["id"].Value);

                var oferta = new
                {
                    title = fila.Cells["title"].Value?.ToString(),
                    description = fila.Cells["description"].Value?.ToString(),
                    location = fila.Cells["location"].Value?.ToString(),
                    modality = fila.Cells["modality"].Value?.ToString(),
                    contractType = fila.Cells["contractType"].Value?.ToString(),
                    salary = ParseDecimalSafe(fila.Cells["salary"].Value)
                };

                string baseUrl =
                    PantallaPrincipal.apiBase.Replace("/auth", "");

                string url =
                    $"{baseUrl}/offers/{id}?sessionId={PantallaPrincipal.SessionId}";

                var json = JsonSerializer.Serialize(oferta);

                var content = new StringContent(
                    json,
                    Encoding.UTF8,
                    "application/json");

                var response = await client.PutAsync(url, content);

                var responseText =
                    await response.Content.ReadAsStringAsync();

                if (response.IsSuccessStatusCode)
                {
                    MessageBox.Show("Oferta actualizada ✔");
                }
                else
                {
                    MessageBox.Show("ERROR:\n" + responseText);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error: " + ex.Message);
            }
        }
        /// <summary>
        /// Convierte un valor de tipo object a decimal de forma segura.
        /// Evita errores cuando el valor es null o tiene formato incorrecto.
        /// </summary>
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
        /// <summary>
        /// Elimina una oferta seleccionada.
        /// </summary>
        private async void btnEliminar_Click(object sender, EventArgs e)
        {
            if (dataGridView1.CurrentRow == null)
            {
                MessageBox.Show("Selecciona una oferta");
                return;
            }

            var oferta =
                (Oferta)dataGridView1.CurrentRow.DataBoundItem;

            var confirm = MessageBox.Show(
                "Segur que vols eliminar aquesta oferta?",
                "Confirmar",
                MessageBoxButtons.YesNo,
                MessageBoxIcon.Warning);

            if (confirm != DialogResult.Yes)
                return;

            try
            {
                string baseUrl =
                    PantallaPrincipal.apiBase.Replace("/auth", "");

                string url =
                    $"{baseUrl}/offers/{oferta.Id}?sessionId={PantallaPrincipal.SessionId}";

                var response = await client.DeleteAsync(url);

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
        /// <summary>
        /// Obre el formulari de candidats de l’oferta seleccionada.
        /// </summary>
        private void btnCandidats_Click(object sender, EventArgs e)
        {
            if (dataGridView1.CurrentRow == null)
            {
                MessageBox.Show("Selecciona una oferta");
                return;
            }

            var oferta =
                (Oferta)dataGridView1.CurrentRow.DataBoundItem;

            var form = new Candidats(oferta.Id);

            form.Show();
        }

        /// <summary>
        /// Botó per tancar l’aplicació
        /// </summary>
        private void btnTancar_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }
        /// <summary>
        /// Torna a la pantalla anterior.
        /// </summary>
        private void btnTornar_Click(object sender, EventArgs e)
        {
            _pantallaEmpresa.Show();
            this.Close();
        }
        /// <summary>
        /// Mostra la pantalla anterior quan es tanca el formulari.
        /// </summary>
        private void OfertesDeTreball_FormClosing(
            object sender,
            FormClosingEventArgs e)
        {
            _pantallaEmpresa.Show();
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }
    }
}