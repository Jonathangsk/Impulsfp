using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Windows.Forms;

namespace IMPULS_Desktop
{
    public partial class PublicarOferta : Form
    {
        private PantallaEmpresa _pantallaEmpresa;

        private readonly string apiBase =
            "http://0bb0dfb7-9b4c-40bc-a0be.5b8c35470a40.bastion.elmeuescriptori.cat:80/offers";

        public PublicarOferta(PantallaEmpresa pantallaEmpresa)
        {
            InitializeComponent();
            _pantallaEmpresa = pantallaEmpresa;

            this.FormClosing += PublicarOferta_FormClosing;
            InicializarCombos();
        }

        public PublicarOferta()
        {
            InitializeComponent();
            InicializarCombos();
        }

       
        private void InicializarCombos()
        {
            comboModalitat.Items.Clear();
            comboTipusdecontracte.Items.Clear();
            comboCycle.Items.Clear();

            comboModalitat.Items.AddRange(new string[] { "REMOTE", "HYBRID", "ONSITE" });
            comboTipusdecontracte.Items.AddRange(new string[] { "PRACTICAS", "JUNIOR", "SENIOR" });
            comboCycle.Items.AddRange(new string[] { "DAM", "DAW", "ASIX" });
        }

        private void PublicarOferta_FormClosing(object sender, FormClosingEventArgs e)
        {
            _pantallaEmpresa?.Show();
        }

     
        private async void btnDesar_Click(object sender, EventArgs e)
        {
            try
            {
                if (string.IsNullOrWhiteSpace(textTitol.Text) ||
                    string.IsNullOrWhiteSpace(textDescripcio.Text) ||
                    string.IsNullOrWhiteSpace(textHabilitats.Text) ||
                    string.IsNullOrWhiteSpace(textUbicacio.Text))
                {
                    MessageBox.Show("Falten camps obligatoris");
                    return;
                }

                if (comboModalitat.SelectedIndex == -1 ||
                    comboTipusdecontracte.SelectedIndex == -1 ||
                    comboCycle.SelectedIndex == -1)
                {
                    MessageBox.Show("Selecciona modalitat, contracte i cicle");
                    return;
                }

                decimal salary = 0;
                decimal.TryParse(textSalari.Text, out salary);

                List<string> skillsList = textHabilitats.Text
                    .Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries)
                    .Select(s => s.Trim())
                    .Where(s => !string.IsNullOrWhiteSpace(s))
                    .ToList();

                var oferta = new
                {
                    title = textTitol.Text,
                    description = textDescripcio.Text,
                    skills = skillsList,
                    location = textUbicacio.Text,

                    modality = comboModalitat.SelectedItem.ToString(),
                    contractType = comboTipusdecontracte.SelectedItem.ToString(),
                    cycle = comboCycle.SelectedItem.ToString(),

                    salary = salary
                };

                using (HttpClient client = new HttpClient())
                {
                    string url = $"{apiBase}?sessionId={PantallaPrincipal.SessionId}";

                    var json = JsonSerializer.Serialize(oferta);

                    var content = new StringContent(json, Encoding.UTF8, "application/json");

                    MessageBox.Show("URL:\n" + url + "\n\nJSON:\n" + json);

                    var response = await client.PostAsync(url, content);
                    var responseText = await response.Content.ReadAsStringAsync();

                    MessageBox.Show(
                        "STATUS: " + (int)response.StatusCode +
                        "\n\nRESPONSE:\n" + responseText
                    );

                    if (response.IsSuccessStatusCode)
                    {
                        MessageBox.Show("Oferta creada correctament ✔");
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("ERROR:\n" + ex.Message);
            }
        }
       
        private void btnTancar_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        private void btnTornar_Click(object sender, EventArgs e)
        {
            _pantallaEmpresa?.Show();
            this.Close();
        }
    }
}