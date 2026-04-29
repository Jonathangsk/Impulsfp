using System;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Windows.Forms;


namespace IMPULS_Desktop
{
    /// <summary>
    /// Formulari per eliminar el compte d’una empresa.
    /// Permet confirmar la supressió i fer la crida a l’API.
    /// </summary>
    public partial class EliminarEmpresa : Form
    {
        // Referència al formulari anterior (pantalla empresa)

        Form empresaForm;

        /// <summary>
        /// Constructor que rep el formulari d’origen per poder tornar enrere.
        /// </summary>
        public EliminarEmpresa(Form empresa)
        {
            InitializeComponent();
            empresaForm = empresa;
        }

        /// <summary>
        /// Botó per tornar a la pantalla anterior sense fer canvis.
        /// </summary>
        private async void btnTornar_Click(object sender, EventArgs e)
        {
            empresaForm.Show();
            this.Close();
        }

        /// <summary>
        /// Botó principal per eliminar el compte de l’empresa.
        /// Fa la petició DELETE a l’API després de confirmar.
        /// </summary>
        private async void button7_Click(object sender, EventArgs e)
        {
            // Confirmació abans d’eliminar el compte
            var confirm = MessageBox.Show(
                "Estàs segur que vols eliminar el compte?",
                "Confirmació",
                MessageBoxButtons.YesNo,
                MessageBoxIcon.Warning
            );

            if (confirm != DialogResult.Yes)
                return;

            try
            {
                using (var client = new HttpClient())
                {
                    // URL de l’API amb la sessió activa

                    var url = $"{PantallaPrincipal.apiBase.Replace("/auth", "")}/users/me?sessionId={PantallaPrincipal.SessionId}";

                    // Cos de la petició (contrasenya per confirmar eliminació)

                    var requestBody = new
                    {
                        password = textContrasenya.Text
                    };

                    var json = JsonSerializer.Serialize(requestBody);

                    // Es crea una petició DELETE amb body

                    var request = new HttpRequestMessage
                    {
                        Method = HttpMethod.Delete,
                        RequestUri = new Uri(url),
                        Content = new StringContent(json, Encoding.UTF8, "application/json")
                    };

                    // Enviament de la petició

                    var response = await client.SendAsync(request);

                    // Comprovació de resposta

                    if (response.IsSuccessStatusCode)
                    {
                        MessageBox.Show("Compte eliminat correctament");

                        // Torna al login
                        PantallaPrincipal formPrincipal = new PantallaPrincipal();
                        formPrincipal.Show();
                        this.Close();
                    }
                    else
                    {
                        var error = await response.Content.ReadAsStringAsync();
                        MessageBox.Show("Error eliminant el compte:\n" + error);
                    }
                }
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
        /// Botó per tancar l’aplicació.
        /// </summary>

        private void tancar_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }
    }
}
