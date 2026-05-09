using System;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using IMPULS_Desktop.Services;
using Newtonsoft.Json;

namespace IMPULS_Desktop
{
    /// <summary>
    /// Formulari que permet canviar la contrasenya d’un usuari.
    /// Conté la lògica de validació i la crida a l’API per actualitzar la contrasenya.
    /// </summary>
    public partial class CanviContrasenya : Form
    {
        /// <summary>
        /// SessionId de l’usuari autenticat.
        /// </summary>
        private readonly string sessionId;
        /// <summary>
        /// Client HTTP per comunicar-se amb l’API.
        /// </summary>
        private readonly HttpClient client;
        /// <summary>
        /// Servei per mostrar alertes i missatges.
        /// </summary>
        private readonly IAlertService alertService;

        /// <summary>
        /// Constructor amb injecció de dependències per testing.
        /// </summary>
        public CanviContrasenya(
            string sessionId,
            HttpClient httpClient = null,
            IAlertService alertService = null)
        {
            InitializeComponent();

            this.sessionId = sessionId;
            this.alertService = alertService ?? new AlertService();

            if (httpClient != null)
            {
                this.client = httpClient;
            }
            else
            {
                var handler = new HttpClientHandler();

                handler.ServerCertificateCustomValidationCallback =
                    (message, cert, chain, errors) => true;

                this.client = new HttpClient(handler);
            }
        }

        private void CanviContrasenya_Load(object sender, EventArgs e)
        {

        }

        /// <summary>
        /// Lògica testeable del canvi de contrasenya.
        /// </summary>
        public async Task RestaurarAsync()
        {
            if (string.IsNullOrWhiteSpace(Contrasenyaactuala.Text) ||
                string.IsNullOrWhiteSpace(NovaContrasenyaa.Text) ||
                string.IsNullOrWhiteSpace(confirmaContrasenyaa.Text))
            {
                await alertService.Mostrar(
                    "Error",
                    "Omple tots els camps",
                    "OK"
                );

                return;
            }

            if (NovaContrasenyaa.Text != confirmaContrasenyaa.Text)
            {
                await alertService.Mostrar(
                    "Error",
                    "Les contrasenyes han de ser iguals",
                    "OK"
                );

                return;
            }

            try
            {
                string baseUrl = PantallaPrincipal.apiBase.Replace("/auth", "");

                string url =
                    $"{baseUrl}/users/password?sessionId={this.sessionId}";

                var data = new
                {
                    currentPassword = Contrasenyaactuala.Text,
                    newPassword = NovaContrasenyaa.Text
                };

                string json = JsonConvert.SerializeObject(data);

                var content = new StringContent(
                    json,
                    Encoding.UTF8,
                    "application/json"
                );

                var request = new HttpRequestMessage(
                    new HttpMethod("PATCH"),
                    url
                )
                {
                    Content = content
                };

                HttpResponseMessage response =
                    await client.SendAsync(request);

                string responseBody =
                    await response.Content.ReadAsStringAsync();

                if (response.IsSuccessStatusCode)
                {
                    await alertService.Mostrar(
                        "OK",
                        "Contrasenya modificada correctament",
                        "OK"
                    );

                    this.Owner?.Show();
                    this.Close();
                }
                else
                {
                    await alertService.Mostrar(
                        "Error",
                        responseBody,
                        "OK"
                    );
                }
            }
            catch (HttpRequestException ex)
            {
                await alertService.Mostrar(
                    "Error HTTP",
                    ex.Message,
                    "OK"
                );
            }
            catch (TaskCanceledException)
            {
                await alertService.Mostrar(
                    "Timeout",
                    "El servidor no respon",
                    "OK"
                );
            }
            catch (Exception ex)
            {
                await alertService.Mostrar(
                    "Error de connexió",
                    ex.Message,
                    "OK"
                );
            }
        }

        /// <summary>
        /// Event del botó restaurar.
        /// Executa el canvi de contrasenya.
        /// </summary>
        public async void restaurar_Click(object sender, EventArgs e)
        {
            await RestaurarAsync();
        }
        /// <summary>
        /// Propietat per obtenir o assignar
        /// la contrasenya actual.
        /// </summary>
        public string TxtContrasenyaActual
        {
            get => Contrasenyaactuala.Text;
            set => Contrasenyaactuala.Text = value;
        }
        /// <summary>
        /// Propietat per obtenir o assignar
        /// la nova contrasenya.
        /// </summary>
        public string TxtNovaContrasenya
        {
            get => NovaContrasenyaa.Text;
            set => NovaContrasenyaa.Text = value;
        }
        /// <summary>
        /// Propietat per obtenir o assignar
        /// la confirmació de contrasenya.
        /// </summary>
        public string TxtConfirmacio
        {
            get => confirmaContrasenyaa.Text;
            set => confirmaContrasenyaa.Text = value;
        }
        /// <summary>
        /// Torna al formulari anterior.
        /// </summary>
        private void tornar_Click(object sender, EventArgs e)
        {
            this.Owner?.Show();
            this.Close();
        }
        /// <summary>
        /// Tanca completament l’aplicació.
        /// </summary>
        private void tancar_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }
    }
}