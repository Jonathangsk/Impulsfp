using System;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace IMPULS_Desktop
{
    /// <summary>
    /// <author>Josep Mª</author>
    /// Formulari principal de l'aplicació IMPULS_Desktop.
    /// Gestiona el login i obre el formulari segons el tipus d'usuari.
    /// </summary>
    public partial class PantallaPrincipal : Form
    {
        public static string SessionId; // ID de sessió de l'usuari
        public static string apiBase = "http://0bb0dfb7-9b4c-40bc-a0be.5b8c35470a40.bastion.elmeuescriptori.cat:80/auth";
       
        public PantallaPrincipal()
        {
            InitializeComponent();
            this.FormClosing += Form1_FormClosing;
        }

        /// <summary>
        /// Valida un usuari mitjançant l'API.
        /// </summary>
        /// <param name="usuari">Nom d'usuari.</param>
        /// <param name="contrasenya">Contrasenya de l'usuari.</param>
        /// <returns>Tupla amb l'èxit del login i el tipus d'usuari.</returns>
        public async Task<(bool Success, string UserType)> ValidarUsuarioAPI(string usuari, string contrasenya)
        {
            try
            {
                using (var client = new HttpClient())
                {
                    client.Timeout = TimeSpan.FromSeconds(5);

                    var url = $"{apiBase}/login";
                    var requestObj = new { username = usuari, password = contrasenya };
                    var json = JsonSerializer.Serialize(requestObj);
                    var content = new StringContent(json, Encoding.UTF8, "application/json");

                    var response = await client.PostAsync(url, content);

                    if (response.IsSuccessStatusCode)
                    {
                        var responseString = await response.Content.ReadAsStringAsync();
                        var loginResponse = JsonSerializer.Deserialize<LoginResponse>(
                            responseString,
                            new JsonSerializerOptions { PropertyNameCaseInsensitive = true }
                        );

                        if (loginResponse != null && !string.IsNullOrWhiteSpace(loginResponse.UserType))
                        {
                            string tipus = loginResponse.UserType.Trim().ToUpper();
                            SessionId = loginResponse.SessionId;
                            return (true, tipus);
                        }
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.Unauthorized)
                    {
                        MessageBox.Show("Usuari o contrasenya incorrectes");
                        return (false, null);
                    }
                }
            }
            catch (HttpRequestException)
            {
                MessageBox.Show("No es pot connectar amb el servidor (Isard apagat o URL incorrecta)");
            }
            catch (TaskCanceledException)
            {
                MessageBox.Show("Temps d'espera esgotat (servidor no respon)");
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error inesperat: " + ex.Message);
            }

            return (false, null);
        }
        /// <summary>
        /// Event del botó de login. Obre el formulari corresponent segons el tipus d'usuari.
        /// </summary>

        private async void button1_Click(object sender, EventArgs e)
        {
            string usuari = textBoxUsuario.Text;
            string contrasenya = textBoxContraseña.Text;

            if (string.IsNullOrWhiteSpace(usuari) || string.IsNullOrWhiteSpace(contrasenya))
            {
                MessageBox.Show("Introdueix usuari i contrasenya");
                return;
            }

            var result = await ValidarUsuarioAPI(usuari, contrasenya);

            if (result.Success)
            {
                string tipus = result.UserType;
                MessageBox.Show($"Login correcte!\nTipus: '{tipus}'");

                Form formulariUsuari = null;
                  //Form formulariUsuari;

                if (tipus == "ADMIN")
                    formulariUsuari = new Administrador();
                else if (tipus == "COMPANY")
                    formulariUsuari = new PantallaEmpresa();
                else if (tipus == "STUDENT")
                    MessageBox.Show("Inicia sessió des de l'aplicació mòbil");

                // formulariUsuari = new Form3();
                else
                {
                    MessageBox.Show($"Tipus desconegut: '{tipus}'");
                    return;
                }
                if (formulariUsuari != null)
                {
                    formulariUsuari.Owner = this;
                    formulariUsuari.FormClosed += FormulariUsuari_FormClosed;
                    formulariUsuari.Show();
                    this.Hide();
                }
            }
        }
        /// <summary>
        /// Quan es tanca un formulari secundari, es torna al login i es netegen camps.
        /// </summary>
        private void FormulariUsuari_FormClosed(object sender, FormClosedEventArgs e)
        {
            this.Show();
            textBoxUsuario.Text = "";
            textBoxContraseña.Text = "";
        }
        /// <summary>
        /// Tanca completament l’aplicació quan es tanca la finestra principal.
        /// </summary>
        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            Application.Exit();
        }



        // Events buits del projecte original
        private void label4_Click(object sender, EventArgs e) { }
        private void textBox1_TextChanged(object sender, EventArgs e) { }
        private void groupBox1_Enter(object sender, EventArgs e) { }
        private void pictureBox1_Click(object sender, EventArgs e) { }
        /// <summary>
        /// Botó de sortida de l’aplicació.
        /// Tanca completament el programa.
        /// </summary>
        private void button2_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }
        /// <summary>
        /// Obre el formulari de canvi de contrasenya.
        /// Amaga la pantalla principal.
        /// </summary>
        private void linkLabel1_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
            CanviContrasenya form = new CanviContrasenya((PantallaPrincipal.SessionId));
            form.Owner = this;   
            form.Show();

            this.Hide();
        }
        /// <summary>
        /// Obre el formulari de registre de nova empresa.
        /// Amaga la pantalla principal.
        /// </summary>
        private void btnRegistreNovaEmpresa_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
            RegistreNovaEmpresa form = new RegistreNovaEmpresa(this);
            form.Owner = this;
            form.Show();

            this.Hide();
        }

        private void textBoxContraseña_TextChanged(object sender, EventArgs e)
        {

        }
    }
}