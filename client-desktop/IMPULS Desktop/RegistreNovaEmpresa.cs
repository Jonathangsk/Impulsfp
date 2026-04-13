using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Xml.Linq;
using System.Text.RegularExpressions;
using static System.Windows.Forms.VisualStyles.VisualStyleElement;
using System.Net.Http;
using System.Text.Json;

namespace IMPULS_Desktop
{
    
    public partial class RegistreNovaEmpresa : Form
    {
        private string rutaImagen;
        private Form formularioAnterior;
        public RegistreNovaEmpresa(Form formAnterior)
        {
            InitializeComponent();
            formularioAnterior = formAnterior;
        }



    private async Task RegistrarEmpresaAPI()
    {
        try
        {
            using (var client = new HttpClient())
            {
                client.Timeout = TimeSpan.FromSeconds(5);

                var url = $"{PantallaPrincipal.apiBase}/register/company";

                var requestObj = new
                {
                    username = textUsuari.Text,
                    password = textContrasenya.Text,
                    name = textNom.Text,
                    email = textEmail.Text,
                    address = textAdreça.Text,
                    vatNumber = textCif.Text,
                    website = textWeb.Text,
                    phone = textTelefon.Text,
                    niche = textSector.Text,
                    technologies = textTecnologies.Text
                        .Split(',')
                        .Select(t => t.Trim())
                        .Where(t => !string.IsNullOrEmpty(t))
                        .ToList()
                };

                var json = JsonSerializer.Serialize(requestObj);
                var content = new StringContent(json, Encoding.UTF8, "application/json");

                var response = await client.PostAsync(url, content);

                if (response.IsSuccessStatusCode)
                {
                    var responseString = await response.Content.ReadAsStringAsync();

                    var result = JsonSerializer.Deserialize<PantallaPrincipal.LoginResponse>(
                        responseString,
                        new JsonSerializerOptions { PropertyNameCaseInsensitive = true }
                    );

                    MessageBox.Show("Empresa registrada i login correcte!");

                    // opcional: guardar sesión
                    PantallaPrincipal.SessionId = result.SessionId;
                }
                else
                {
                    var error = await response.Content.ReadAsStringAsync();
                    MessageBox.Show("Error del servidor: " + error);
                }
            }
        }
        catch (HttpRequestException)
        {
            MessageBox.Show("No es pot connectar amb el servidor (Isard apagat)");
        }
        catch (TaskCanceledException)
        {
            MessageBox.Show("Temps d'espera esgotat");
        }
        catch (Exception ex)
        {
            MessageBox.Show("Error: " + ex.Message);
        }
    }

    private void perfil_Click(object sender, EventArgs e)
        {

        }

        private void textEmail_TextChanged(object sender, EventArgs e)
        {

        }

        private async void btnDesar_Click(object sender, EventArgs e) 
        {
            string username = textUsuari.Text;
            string password = textContrasenya.Text;

            // VALIDACIONES
            if (!ValidarUsername(username))
            {
                MessageBox.Show("El nom d'usuari ha de tenir entre 4 i 20 caràcters i només lletres i números.");
                return;
            }

            if (!ValidarPassword(password))
            {
                MessageBox.Show("La contrasenya ha de tenir mínim 6 caràcters, amb almenys una majúscula, una minúscula i un número.");
                return;
            }
            Empresa empresa = new Empresa();

            empresa.Username = textUsuari.Text;        
            empresa.Password = textContrasenya.Text;
            empresa.Name = textNom.Text;
            empresa.Email = textEmail.Text;
            empresa.Address = textAdreça.Text;
            empresa.VatNumber = textCif.Text;
            empresa.Website = textWeb.Text;
            empresa.Phone = textTelefon.Text;
            empresa.Niche = textSector.Text;
            var technologiesList = textTecnologies.Text
                                                .Split(',')
                                                .Select(t => t.Trim())
                                                .Where(t => !string.IsNullOrEmpty(t))
                                                .ToList();


         //   empresa.ProfilePhoto = rutaImagen;
            MessageBox.Show("Empresa guardada correctament");
            await RegistrarEmpresaAPI();
        }

        private void OfertesActives_SelectedIndexChanged(object sender, EventArgs e)
        { }
            private void CargarOfertas(Empresa empresa)
        {
            OfertesActives.Items.Clear();

            foreach (Oferta o in empresa.ActiveOffers)
            {
                OfertesActives.Items.Add(o.Title);
            }
        }

        private void textBox1_TextChanged(object sender, EventArgs e)
        {

        }
        
     /*         USUARI
     *          4 a 20 caracters nomes lletres i números
     */

private bool ValidarUsername(string username)
    {
        return Regex.IsMatch(username, @"^[a-zA-Z0-9]{4,20}$");
    }

    /*        CONTRASENYA
     *        mínimo 6 caracteres
     *        1 mayúscula
     *        1 minúscula
     *        1 número*/
    private bool ValidarPassword(string password)
        {
            return Regex.IsMatch(password, @"^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{6,}$");
        }

        private void btnReset_Click(object sender, EventArgs e)
        {
            textUsuari.Clear();
            textContrasenya.Clear();
            textNom.Clear();
            textEmail.Clear();
            textAdreça.Clear();
            textCif.Clear();
            textWeb.Clear();
            textTelefon.Clear();
            textSector.Clear();
            textTecnologies.Clear();


            MessageBox.Show("Formulari net");
        }

        private void btnTornar_Click(object sender, EventArgs e)
        {
            formularioAnterior.Show(); 
            this.Close();
        }

        private void btnTancar_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        private void label7_Click(object sender, EventArgs e)
        {

        }
    }
    }

