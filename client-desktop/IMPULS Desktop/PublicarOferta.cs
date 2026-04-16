using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Windows.Forms;


namespace IMPULS_Desktop
{
    public partial class PublicarOferta : Form
    {
        private PantallaEmpresa _pantallaEmpresa;
        private readonly string apiBase = "http://localhost:8080/offers";
        public PublicarOferta(PantallaEmpresa pantallaEmpresa)
        {
            InitializeComponent();
            _pantallaEmpresa = pantallaEmpresa;

            this.FormClosing += PublicarOferta_FormClosing;
        }
        public PublicarOferta()
        {
            InitializeComponent();
            comboModalitat.Items.AddRange(new string[] { "Practicas", "Junior", "Senior" });
            comboTipusdecontracte.Items.AddRange(new string[] { "Remot", "Hibrid", "Presencial" });
            comboTipusdecontracte.DropDownWidth = 150;
            comboCicle.Items.AddRange(new string[] { "ASIX", "DAM", "DAW" });
            comboEstatdelaoferta.Items.AddRange(new string[] { "Activa", "Expired" });


        }
        private void PublicarOferta_FormClosing(object sender, FormClosingEventArgs e)
        {
            _pantallaEmpresa.Show();
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }



        private void label7_Click(object sender, EventArgs e)
        {

        }

        private void textBox3_TextChanged(object sender, EventArgs e)
        {

        }

        private void groupBox1_Enter(object sender, EventArgs e)
        {

        }


        private void label15_Click(object sender, EventArgs e)
        {

        }

        private void textBox8_TextChanged(object sender, EventArgs e)
        {

        }



        private void PublicarOferta_Load(object sender, EventArgs e)
        {

        }



        private void textBox6_TextChanged(object sender, EventArgs e)
        {

        }

        private void textBox7_TextChanged(object sender, EventArgs e)
        {

        }

        private void textBox1_TextChanged(object sender, EventArgs e)
        {

        }

        private void textBox2_TextChanged(object sender, EventArgs e)
        {

        }

        private void textBox3_TextChanged_1(object sender, EventArgs e)
        {

        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {

        }

        private void comboBox4_SelectedIndexChanged(object sender, EventArgs e)
        {

        }


        private void label8_Click(object sender, EventArgs e)
        {

        }

        private void label5_Click(object sender, EventArgs e)
        {

        }

        private void label9_Click(object sender, EventArgs e)
        {

        }

        private void label14_Click(object sender, EventArgs e)
        {

        }

        private void label3_Click(object sender, EventArgs e)
        {

        }

        private void label10_Click(object sender, EventArgs e)
        {

        }

        private void textBox7_TextChanged_1(object sender, EventArgs e)
        {

        }

        private void textBox10_TextChanged(object sender, EventArgs e)
        {

        }

        private async void btnDesar_Click(object sender, EventArgs e)
        {
            try
            {
                // Validacions

                if (string.IsNullOrWhiteSpace(textTitol.Text))
                {
                    MessageBox.Show("El títol és obligatori");
                    return;
                }

                if (string.IsNullOrWhiteSpace(textDescripcio.Text))
                {
                    MessageBox.Show("La descripció és obligatòria");
                    return;
                }

                if (string.IsNullOrWhiteSpace(textEmpresa.Text))
                {
                    MessageBox.Show("L'empresa és obligatòria");
                    return;
                }

                if (string.IsNullOrWhiteSpace(textHabilitats.Text))
                {
                    MessageBox.Show("Les habilitats són obligatòries");
                    return;
                }

                if (string.IsNullOrWhiteSpace(textUbicacio.Text))
                {
                    MessageBox.Show("La ubicació és obligatòria");
                    return;
                }

                if (comboModalitat.SelectedIndex == -1)
                {
                    MessageBox.Show("Selecciona una modalitat");
                    return;
                }

                if (comboTipusdecontracte.SelectedIndex == -1)
                {
                    MessageBox.Show("Selecciona un tipus de contracte");
                    return;
                }

                if (comboCicle.SelectedIndex == -1)
                {
                    MessageBox.Show("Selecciona un cicle");
                    return;
                }

                if (comboEstatdelaoferta.SelectedIndex == -1)
                {
                    MessageBox.Show("Selecciona l'estat de la oferta");
                    return;
                }

                // salari es opcional

                decimal salarioFinal = 0;
                if (!string.IsNullOrWhiteSpace(textSalari.Text))
                {
                    decimal.TryParse(textSalari.Text, out salarioFinal);
                }

                //Creem l'objecte
                Oferta oferta = new Oferta
                {
                    Id = string.IsNullOrWhiteSpace(textIdentificador.Text) ? 0 : int.Parse(textIdentificador.Text),

                    Title = textTitol.Text,
                    Description = textDescripcio.Text,

                    Company = new Empresa
                    {
                        Name = textEmpresa.Text
                    },

                    RequiredSkills = textHabilitats.Text,
                    Location = textUbicacio.Text,

                    Modality = comboModalitat.SelectedItem.ToString(),
                    ContractType = comboTipusdecontracte.SelectedItem.ToString(),

                    Salary = salarioFinal,

                    CreationDate = DateTime.Now,

                    Applicants = new List<string>(),

                    Cicle = comboCicle.SelectedItem.ToString(),
                    Estat = comboEstatdelaoferta.SelectedItem.ToString(),

                    Observacions = textObservacions.Text
                };

                // Enviem a la api
                using (HttpClient client = new HttpClient())
                {
                    var json = JsonSerializer.Serialize(oferta);
                    var content = new StringContent(json, Encoding.UTF8, "application/json");

                    var response = await client.PostAsync(apiBase, content);

                    if (response.IsSuccessStatusCode)
                    {
                        MessageBox.Show("Oferta publicada correctament");
                        LimpiarFormulari();
                    }
                    else
                    {
                        var error = await response.Content.ReadAsStringAsync();
                        MessageBox.Show("Error en publicar la oferta: " + error);
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error: " + ex.Message);
            }
        }
        private void LimpiarFormulari()
        {
            textIdentificador.Clear();
            textTitol.Clear();
            textDescripcio.Clear();
            textEmpresa.Clear();
            textHabilitats.Clear();
            textUbicacio.Clear();
            textSalari.Clear();
            textDatadecreacio.Clear();
            textCandidats.Clear();
            textObservacions.Clear();

            comboModalitat.SelectedIndex = -1;
            comboTipusdecontracte.SelectedIndex = -1;
            comboCicle.SelectedIndex = -1;
            comboEstatdelaoferta.SelectedIndex = -1;
        }

        private void btnTancar_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        private void btnTornar_Click(object sender, EventArgs e)
        {
            _pantallaEmpresa.Show();
            this.Close();
        }
    }
}


