using System;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace IMPULS_Desktop
{
    /// <summary>
    /// Formulari per eliminar el compte d’una empresa.
    /// Permet confirmar la supressió i fer la crida a l’API.
    /// </summary>
    public partial class EliminarEmpresa : Form
    {
        // Referència al formulari anterior
        Form empresaForm;

        // Client HTTP amb bypass SSL
        private readonly HttpClient client;

        /// <summary>
        /// Constructor que rep el formulari d’origen
        /// </summary>
        public EliminarEmpresa(Form empresa)
        {
            InitializeComponent();

            empresaForm = empresa;

            var handler = new HttpClientHandler();

            handler.ServerCertificateCustomValidationCallback =
                (message, cert, chain, errors) => true;

            client = new HttpClient(handler);
        }

        /// <summary>
        /// Botó per tornar enrere
        /// </summary>
        private void btnTornar_Click(object sender, EventArgs e)
        {
            empresaForm.Show();
            this.Close();
        }

        /// <summary>
        /// Botó principal per eliminar el compte
        /// </summary>
        private async void button7_Click(object sender, EventArgs e)
        {
            var confirm = MessageBox.Show(
                "Estàs segur que vols eliminar el compte?",
                "Confirmació",
                MessageBoxButtons.YesNo,
                MessageBoxIcon.Warning
            );

            if (confirm != DialogResult.Yes)
                return;

            if (string.IsNullOrWhiteSpace(textContrasenya.Text))
            {
                MessageBox.Show("Introdueix la contrasenya");
                return;
            }

            try
            {
                string url =
                    $"{PantallaPrincipal.apiBase.Replace("/auth", "")}/users/me?sessionId={PantallaPrincipal.SessionId}";

                var requestBody = new
                {
                    password = textContrasenya.Text
                };

                var json = JsonSerializer.Serialize(requestBody);

                var request = new HttpRequestMessage
                {
                    Method = HttpMethod.Delete,
                    RequestUri = new Uri(url),
                    Content = new StringContent(
                        json,
                        Encoding.UTF8,
                        "application/json"
                    )
                };

                var response = await client.SendAsync(request);

                if (response.IsSuccessStatusCode)
                {
                    MessageBox.Show("Compte eliminat correctament");

                    PantallaPrincipal formPrincipal =
                        new PantallaPrincipal();

                    formPrincipal.Show();

                    this.Close();
                }
                else
                {
                    var error =
                        await response.Content.ReadAsStringAsync();

                    MessageBox.Show(
                        "Error eliminant el compte:\n" + error
                    );
                }
            }
            catch (HttpRequestException ex)
            {
                MessageBox.Show("Error HTTP: " + ex.Message);
            }
            catch (TaskCanceledException)
            {
                MessageBox.Show("Timeout amb el servidor");
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error: " + ex.Message);
            }
        }

        private void textUsuari_TextChanged(object sender, EventArgs e)
        {

        }

        /// <summary>
        /// Botó per tancar l’aplicació
        /// </summary>
        private void tancar_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }
    }
}