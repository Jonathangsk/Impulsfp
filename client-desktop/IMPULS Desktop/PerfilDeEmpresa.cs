using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace IMPULS_Desktop
{
    /// <summary>
    /// <author>Josep Mª</author>
    /// Formulari de perfil d’empresa.
    /// </summary>
    public partial class PerfilDeEmpresa : Form
    {
        private readonly IApiClient _api = new ApiClient();
        private PantallaEmpresa _pantallaEmpresa;
        private Empresa empresaActual;
        private bool _guardat = false;

        // CLIENT HTTPS CON CERTIFICADO IGNORADO
        private static readonly HttpClient client;

        static PerfilDeEmpresa()
        {
            var handler = new HttpClientHandler();

            handler.ServerCertificateCustomValidationCallback =
                (message, cert, chain, errors) => true;

            client = new HttpClient(handler);

            client.Timeout = TimeSpan.FromSeconds(30);
        }
        /// <summary>
        /// Constructor del formulari amb referència a la pantalla d’empresa.
        /// Carrega dades inicials i assigna events als DataGridView.
        /// </summary>
        public PerfilDeEmpresa(PantallaEmpresa pantallaEmpresa)
        {
            InitializeComponent();

            _pantallaEmpresa = pantallaEmpresa;

            this.FormClosing += PerfilEmpresa_FormClosing;
            this.Load += async (s, e) => await CarregarDades();

            dataGridView1.SelectionChanged += dataGridView1_SelectionChanged;
            dataGridView1.CellEndEdit += dataGridView1_CellEndEdit;
            dataGridView2.CellEndEdit += dataGridView2_CellEndEdit;
        }
        /// <summary>
        /// Quan es tanca el formulari, es torna a mostrar la pantalla d’empresa.
        /// </summary>
        private void PerfilEmpresa_FormClosing(object sender, FormClosingEventArgs e)
        {
            _pantallaEmpresa.Show();
        }
        /// <summary>
        /// Carrega les dades de l’empresa autenticada des de l’API.
        /// També inicialitza el DataGridView d’empresa i ofertes.
        /// </summary>
        private async Task CarregarDades()
        {
            List<Empresa> lista = new List<Empresa>();

            try
            {
                string baseUrl = PantallaPrincipal.apiBase.Replace("/auth", "");

                string url =
                    $"{baseUrl}/users/me?sessionId={PantallaPrincipal.SessionId}";

                var json = await client.GetStringAsync(url);

                var empresa = JsonSerializer.Deserialize<Empresa>(
                    json,
                    new JsonSerializerOptions
                    {
                        PropertyNameCaseInsensitive = true
                    });

                if (empresa != null)
                {
                    empresaActual = empresa;

                    empresa.TechnologiesText =
                        string.Join(", ", empresa.Technologies ?? new List<string>());

                    lista.Add(empresa);
                }

                dataGridView1.AutoGenerateColumns = true;
                dataGridView1.DataSource = lista;
                dataGridView1.ReadOnly = false;
                dataGridView1.AllowUserToAddRows = false;
                dataGridView1.SelectionMode = DataGridViewSelectionMode.FullRowSelect;

                if (dataGridView1.Columns.Contains("Technologies"))
                    dataGridView1.Columns["Technologies"].Visible = false;

                if (dataGridView1.Columns.Contains("Password"))
                    dataGridView1.Columns["Password"].Visible = false;

                dataGridView2.AutoGenerateColumns = true;
                dataGridView2.DataSource = new List<Oferta>();
                dataGridView2.ReadOnly = false;
                dataGridView2.AllowUserToAddRows = false;
                dataGridView2.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error carregant dades: " + ex.Message);
            }
        }
        /// <summary>
        /// Quan es selecciona una empresa al DataGridView,
        /// es carreguen les seves ofertes.
        /// </summary>
        private async void dataGridView1_SelectionChanged(object sender, EventArgs e)
        {
            try
            {
                if (dataGridView1.CurrentRow == null) return;

                var empresa = (Empresa)dataGridView1.CurrentRow.DataBoundItem;

                if (empresa == null) return;

                await CarregarOfertesEmpresa(empresa.Id);
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error cargando ofertas: " + ex.Message);
            }
        }
        /// <summary>
        /// Carrega les ofertes de l’empresa des de l’API.
        /// </summary>
        private async Task CarregarOfertesEmpresa(int empresaId)
        {
            try
            {
                string baseUrl = PantallaPrincipal.apiBase.Replace("/auth", "");

                string url =
                    $"{baseUrl}/offers/my?sessionId={PantallaPrincipal.SessionId}";

                var json = await client.GetStringAsync(url);

                var offers = JsonSerializer.Deserialize<List<Oferta>>(
                    json,
                    new JsonSerializerOptions
                    {
                        PropertyNameCaseInsensitive = true
                    });

                dataGridView2.DataSource = offers ?? new List<Oferta>();

                if (dataGridView2.Columns.Contains("skills"))
                    dataGridView2.Columns["skills"].Visible = false;
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error cargando ofertas: " + ex.Message);
            }
        }

        /// <summary>
        /// Quan s’edita una cel·la de l’empresa,
        /// es valida el camp i es fa update a l’API.
        /// </summary>
        private async void dataGridView1_CellEndEdit(object sender, DataGridViewCellEventArgs e)
        {
            try
            {
                if (empresaActual == null) return;

                var columnName = dataGridView1.Columns[e.ColumnIndex].Name;

                if (columnName == "Id" ||
                    columnName == "Email" ||
                    columnName == "VatNumber" ||
                    columnName == "Username" ||
                    columnName == "ActiveOffers")
                {
                    MessageBox.Show("Aquest camp no es pot editar");
                    await CarregarDades();
                    return;
                }

                var body = new
                {
                    name = empresaActual.Name,
                    address = empresaActual.Address,
                    phone = empresaActual.Phone,
                    website = empresaActual.Website,
                    niche = empresaActual.Niche,
                    technologies = empresaActual.Technologies ?? new List<string>()
                };

                string baseUrl = PantallaPrincipal.apiBase.Replace("/auth", "");

                string url =
                    $"{baseUrl}/users/me?sessionId={PantallaPrincipal.SessionId}";

                var json = JsonSerializer.Serialize(body);

                var content = new StringContent(
                    json,
                    Encoding.UTF8,
                    "application/json");

                var response = await client.PutAsync(url, content);

                var responseText = await response.Content.ReadAsStringAsync();

                if (response.IsSuccessStatusCode)
                {
                    MessageBox.Show("Dades guardades correctament");
                    await CarregarDades();
                }
                else
                {
                    MessageBox.Show("ERROR BACKEND:\n" + responseText);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error: " + ex.Message);
            }
        }
        /// <summary>
        /// Converteix un valor a decimal de forma segura.
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
        /// Actualitza una oferta quan s’edita al DataGridView.
        /// </summary>
        private async void dataGridView2_CellEndEdit(object sender, DataGridViewCellEventArgs e)
        {
            try
            {
                if (e.RowIndex < 0) return;

                var fila = dataGridView2.Rows[e.RowIndex];

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

                string baseUrl = PantallaPrincipal.apiBase.Replace("/auth", "");

                string url =
                    $"{baseUrl}/offers/{id}?sessionId={PantallaPrincipal.SessionId}";

                var json = JsonSerializer.Serialize(oferta);

                var content = new StringContent(
                    json,
                    Encoding.UTF8,
                    "application/json");

                var response = await client.PutAsync(url, content);

                var responseText = await response.Content.ReadAsStringAsync();

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
        /// Elimina una oferta seleccionada després de confirmació.
        /// </summary>
        private async void btnEliminar_Click(object sender, EventArgs e)
        {
            if (dataGridView2.CurrentRow == null)
            {
                MessageBox.Show("Selecciona una oferta");
                return;
            }

            var oferta = (Oferta)dataGridView2.CurrentRow.DataBoundItem;

            var confirm = MessageBox.Show(
                "Segur que vols eliminar aquesta oferta?",
                "Confirmar",
                MessageBoxButtons.YesNo,
                MessageBoxIcon.Warning);

            if (confirm != DialogResult.Yes)
                return;

            try
            {
                string baseUrl = PantallaPrincipal.apiBase.Replace("/auth", "");

                string url =
                    $"{baseUrl}/offers/{oferta.Id}?sessionId={PantallaPrincipal.SessionId}";

                var response = await client.DeleteAsync(url);

                if (response.IsSuccessStatusCode)
                {
                    MessageBox.Show("Oferta eliminada");
                    await CarregarDades();
                }
                else
                {
                    string error = await response.Content.ReadAsStringAsync();
                    MessageBox.Show(error);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error inesperat: " + ex.Message);
            }
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
        /// Tanca l’aplicació.
        /// </summary>
        private void btnTancar_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

    

        private void PerfilDeEmpresa_Load(object sender, EventArgs e)
        {

        }

        private void dataGridView2_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        private void CanviContrasenya_Click(object sender, EventArgs e)
        {
            CanviContrasenya form =
                new CanviContrasenya(PantallaPrincipal.SessionId);

            form.Owner = this;
            form.ShowDialog();
        }

        private void label2_Click(object sender, EventArgs e)
        {

        }

        private void label1_Click(object sender, EventArgs e)
        {

        }
    }
}