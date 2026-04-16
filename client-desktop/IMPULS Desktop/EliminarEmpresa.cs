using System;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Windows.Forms;


namespace IMPULS_Desktop
{
    public partial class EliminarEmpresa : Form
    {
        Form empresaForm;
        public EliminarEmpresa(Form empresa)
        {
            InitializeComponent();
            empresaForm = empresa;
        }

        private async void btnTornar_Click(object sender, EventArgs e)
        {
            empresaForm.Show();
            this.Close();
        }

      



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

            try
            {
                using (var client = new HttpClient())
                {
                    var url = $"{PantallaPrincipal.apiBase.Replace("/auth", "")}/users/me?sessionId={PantallaPrincipal.SessionId}";

                    // 🔐 AQUI coges la contraseña del TextBox
                    var requestBody = new
                    {
                        password = textContrasenya.Text
                    };

                    var json = JsonSerializer.Serialize(requestBody);

                    var request = new HttpRequestMessage
                    {
                        Method = HttpMethod.Delete,
                        RequestUri = new Uri(url),
                        Content = new StringContent(json, Encoding.UTF8, "application/json")
                    };

                    var response = await client.SendAsync(request);

                    if (response.IsSuccessStatusCode)
                    {
                        MessageBox.Show("Compte eliminat correctament");
                        
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

 

        private void tancar_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }
    }
}
