using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows.Forms;
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



        private async Task<bool> RegistrarEmpresaAPI(Empresa empresa, List<string> technologiesList)
        {
            try
            {
                using (var client = new HttpClient())
                {
                    client.Timeout = TimeSpan.FromSeconds(5);

                    var url = $"{PantallaPrincipal.apiBase}/register/company";

                    var requestObj = new
                    {
                      
                        username = empresa.Username,
                        password = empresa.Password,
                        name = empresa.Name,
                        email = empresa.Email,
                        address = empresa.Address,
                        vatNumber = empresa.VatNumber,
                        website = empresa.Website,
                        phone = empresa.Phone,
                        niche = empresa.Niche,
                        technologies = technologiesList
                    };

                    var json = JsonSerializer.Serialize(requestObj);
                    var content = new StringContent(json, Encoding.UTF8, "application/json");

                    var response = await client.PostAsync(url, content);

                    if (response.IsSuccessStatusCode)
                    {
                        var responseString = await response.Content.ReadAsStringAsync();

                        var result = JsonSerializer.Deserialize<PantallaPrincipal.LoginResponse>(
                            responseString,
                            new JsonSerializerOptions
                            {
                                PropertyNameCaseInsensitive = true
                            }
                        );

                        MessageBox.Show("Empresa registrada i login correcte!");

                        // Guardar sessió
                        PantallaPrincipal.SessionId = result.SessionId;

                        return true; 
                    }
                    else
                    {
                        var error = await response.Content.ReadAsStringAsync();
                        MessageBox.Show("Error del servidor: " + error);

                        return false; // Error del servidor
                    }
                }
            }
            catch (HttpRequestException)
            {
                MessageBox.Show("No es pot connectar amb el servidor (Isard apagat)");
                return false;
            }
            catch (TaskCanceledException)
            {
                MessageBox.Show("Temps d'espera esgotat");
                return false;
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error: " + ex.Message);
                return false;
            }
        }

        private void perfil_Click(object sender, EventArgs e)
        {

        }

        private void textEmail_TextChanged(object sender, EventArgs e)
        {

        }

    
            public async void btnDesar_Click(object sender, EventArgs e)
        {
            string username = textUsuari.Text.Trim();
            string password = textContrasenya.Text.Trim();
            string nom = textNom.Text.Trim();
            string email = textEmail.Text.Trim();
            string adreca = textAdreça.Text.Trim();
            string cif = textCif.Text.Trim();
            string website = textWeb.Text.Trim();
            string telefon = textTelefon.Text.Trim();
            string sector = textSector.Text.Trim();
            string technologies = textTecnologies.Text.Trim();

            // Validem els camps buits
            if (string.IsNullOrEmpty(username))
            {
                MessageBox.Show("El camp 'Usuari' és obligatori");
                return;
            }

            if (string.IsNullOrEmpty(password))
            {
                MessageBox.Show("El camp 'Contrasenya' és obligatori");
                return;
            }

            if (string.IsNullOrEmpty(nom))
            {
                MessageBox.Show("El camp 'Nom' és obligatori");
                return;
            }

            if (string.IsNullOrEmpty(email))
            {
                MessageBox.Show("El camp 'Email' és obligatori");
                return;
            }

            if (string.IsNullOrEmpty(adreca))
            {
                MessageBox.Show("El camp 'Adreça' és obligatori");
                return;
            }

            if (string.IsNullOrEmpty(cif))
            {
                MessageBox.Show("El camp 'CIF' és obligatori");
                return;
            }

            if (string.IsNullOrEmpty(telefon))
            {
                MessageBox.Show("El camp 'Telèfon' és obligatori");
                return;
            }
            if (string.IsNullOrEmpty(website))
            {
                MessageBox.Show("El camp 'Website' és obligatori");
                return;
            }
            if (string.IsNullOrEmpty(sector))
            {
                MessageBox.Show("El camp 'Sector' és obligatori");
                return;
            }
            if (string.IsNullOrEmpty(technologies))
            {
                MessageBox.Show("El camp 'Tecnologies' és obligatori");
                return;
            }

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

            
            Empresa empresa = new Empresa
            {
                Username = textUsuari.Text,
                Password = textContrasenya.Text,
                Name = textNom.Text,
                Email = textEmail.Text,
                Address = textAdreça.Text,
                VatNumber = textCif.Text,
                Website = textWeb.Text,
                Phone = textTelefon.Text,
                Niche = textSector.Text
            };

            var technologiesList = textTecnologies.Text
                .Split(',')
                .Select(t => t.Trim())
                .Where(t => !string.IsNullOrEmpty(t))
                .ToList();

            bool ok = await RegistrarEmpresaAPI(empresa, technologiesList);


            // Nomes neteja si tot es correcte
            if (ok)
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

                formularioAnterior.Show();
                this.Close();
            }
            
        }


        private void textBox1_TextChanged(object sender, EventArgs e)
        {

        }
        
     /*         USUARI
     *          4 a 20 caracters nomes lletres i números
     */

public bool ValidarUsername(string username)
    {
        return Regex.IsMatch(username, @"^[a-zA-Z0-9]{4,20}$");
    }

    /*        CONTRASENYA
     *        mínimo 6 caracteres
     *        1 mayúscula
     *        1 minúscula
     *        1 número*/
    public bool ValidarPassword(string password)
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

        private void label2_Click(object sender, EventArgs e)
        {

        }
    }
    }

