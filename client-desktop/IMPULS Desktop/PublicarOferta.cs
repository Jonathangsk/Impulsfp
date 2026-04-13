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
        private readonly string apiBase = "http://localhost:8080/offers";

        public PublicarOferta()
        {
            InitializeComponent();
            comboModalitat.Items.AddRange(new string[] { "Practicas", "Junior", "Senior" });
            comboTipusdecontracte.Items.AddRange(new string[] { "Remot", "Hibrid", "Presencial" });
            comboTipusdecontracte.DropDownWidth = 150;
            comboCicle.Items.AddRange(new string[] { "ASIX", "DAM", "DAW" });
            comboEstatdelaoferta.Items.AddRange(new string[] { "Activa", "Expired" });






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
                //Validació mínima
                if (string.IsNullOrWhiteSpace(textTitol.Text))
                {
                    MessageBox.Show("El títul es obligatori");
                    return;
                }

                Oferta oferta = new Oferta
                {
                    Id = string.IsNullOrWhiteSpace(textIdentificador.Text) ? 0 : int.Parse(textIdentificador.Text),

                    Title = textTitol.Text,
                    Description = textDescripcio.Text,

                    Company = new Empresa
                    {
                        Name = textEmpresa.Text
                    }, // 👈 AQUÍ VA COMA

                    RequiredSkills = textHabilitats.Text,
                    Location = textUbicacio.Text,

                    Modality = comboModalitat.SelectedItem?.ToString(),
                    ContractType = comboTipusdecontracte.SelectedItem?.ToString(),

                    Salary = decimal.TryParse(textSalari.Text, out decimal sal) ? sal : 0,

                    CreationDate = DateTime.Now,

                    Applicants = new List<string>(),

                    Cicle = comboCicle.SelectedItem?.ToString(),
                    Estat = comboEstatdelaoferta.SelectedItem?.ToString(),

                    Observacions = textObservacions.Text
                };
                //Enviem a la API
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
                        MessageBox.Show("Error en publicar la oferta");
                        MessageBox.Show("Error API: " + error);
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
            this.Close();
        }
    }
}


