using System;
using System.Net.Http;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace IMPULS_Desktop
{
    /// <summary>
    /// <author>Josep Mª</author>
    /// Formulari de gestió d'usuari administratiu.
    /// Permet tancar sessió (logout) de l'aplicació contra l'API REST.
    /// </summary>
    public partial class Administrador : Form
    {
        /// <summary>
        /// Client HTTP reutilitzable per fer peticions a l'API.
        /// </summary>
        private static readonly HttpClient client = new HttpClient();

        /// <summary>
        /// Constructor del formulari.
        /// Inicialitza els components del formulari.
        /// </summary>
        public Administrador()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Esdeveniment de càrrega del formulari.
        /// Actualment sense funcionalitat addicional.
        /// </summary>
        private void Form2_Load(object sender, EventArgs e)
        {

        }

        /// <summary>
        /// Realitza el logout de l'usuari a l'API.
        /// </summary>
        /// <returns>true si el logout és correcte, false en cas contrari.</returns>

        private async Task<bool> LogoutAPI()
        {
            // URL correcte del backend Spring Boot
            string url = $"http://localhost:8080/auth/logout?sessionId={PantallaPrincipal.SessionId}";

            try
            {
                // Enviament de la petició POST (body null)
                HttpResponseMessage response = await client.PostAsync(url, null);

                string responseBody = await response.Content.ReadAsStringAsync();

                if (response.IsSuccessStatusCode)
                {
                    MessageBox.Show("Sessió finalitzada correctament");
                    return true;
                }
                else
                {
                    MessageBox.Show("Error al tancar sessió: " + responseBody);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error en fer logout: " + ex.Message);
            }

            return false;
        }

        /// <summary>
        /// Event del botó de logout.
        /// Invoca LogoutAPI i, si és correcte, neteja la sessió i tanca el formulari.
        /// </summary>
        private async void button1_Click(object sender, EventArgs e)
        {
            bool ok = await LogoutAPI();

            if (ok)
            {
                // Neteja de la sessió
                PantallaPrincipal.SessionId = null;

                // Tancament del formulari i mostrar Form1
                this.Close();
                this.Owner?.Show();
            }
        }
        // Events buits del projecte original
        private void label1_Click(object sender, EventArgs e) { }
        private void groupBox1_Enter(object sender, EventArgs e) { }
        private void pictureBox1_Click(object sender, EventArgs e) { }
    }
}