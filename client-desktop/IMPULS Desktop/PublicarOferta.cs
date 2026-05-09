using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using System.Windows.Forms;

/// <summary>
/// <author>Josep Mª</author>
/// Formulari per crear i publicar ofertes de feina.
/// Permet introduir dades, validar-les i enviar-les a l’API.
/// </summary>
namespace IMPULS_Desktop
{
    /// <summary>
    /// Formulari per crear i publicar ofertes de feina.
    /// </summary>
    public partial class PublicarOferta : Form
    {
        private PantallaEmpresa _pantallaEmpresa;

        // URL BASE CORRECTA HTTPS
        private readonly string apiBase =
            "https://0bb0dfb7-9b4c-40bc-a0be.5b8c35470a40.bastion.elmeuescriptori.cat/offers";

        /// <summary>
        /// Constructor alternatiu sense pantalla anterior.
        /// Només inicialitza el formulari i els combos.
        /// </summary>
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

        /// <summary>
        /// Inicialitza els ComboBox.
        /// </summary>
        private void InicializarCombos()
        {
            comboModalitat.Items.Clear();
            comboTipusdecontracte.Items.Clear();
            comboCycle.Items.Clear();
            comboTest.Items.Clear();

            comboModalitat.Items.AddRange(
                new string[] { "REMOTE", "HYBRID", "ONSITE" });

            comboTipusdecontracte.Items.AddRange(
                new string[]
                {
                    "FP_DUAL",
                    "FCT",
                    "PRACTICAS_EXTRA",
                    "CONTRATO_FORMACION"
                });

            comboCycle.Items.AddRange(
                new string[] { "DAM", "DAW", "ASIX" });

            comboTest.Items.AddRange(
                new string[]
                {
                    "JAVA",
                    "PYTHON",
                    "JAVASCRIPT",
                    "SQL",
                    "CSHARP",
                    "KOTLIN"
                });
        }

        /// <summary>
        /// Quan es tanca el formulari.
        /// </summary>
        private void PublicarOferta_FormClosing(
            object sender,
            FormClosingEventArgs e)
        {
            _pantallaEmpresa?.Show();
        }

        /// <summary>
        /// Crear oferta.
        /// </summary>
        private async void btnDesar_Click(object sender, EventArgs e)
        {
            try
            {
                // VALIDACIÓ
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
                    comboCycle.SelectedIndex == -1 ||
                    comboTest.SelectedIndex == -1)
                {
                    MessageBox.Show("Selecciona tots els combos");
                    return;
                }

                decimal salary = 0;

                decimal.TryParse(textSalari.Text, out salary);

                // SKILLS
                List<string> skillsList = textHabilitats.Text
                    .Split(
                        new char[] { ',' },
                        StringSplitOptions.RemoveEmptyEntries)
                    .Select(s => s.Trim())
                    .Where(s => !string.IsNullOrWhiteSpace(s))
                    .ToList();

                // OBJECTE OFERTA
                var oferta = new
                {
                    title = textTitol.Text,
                    description = textDescripcio.Text,
                    skills = skillsList,
                    location = textUbicacio.Text,

                    modality = comboModalitat.SelectedItem.ToString(),

                    contractType =
                        comboTipusdecontracte.SelectedItem.ToString(),

                    cycle = comboCycle.SelectedItem.ToString(),

                    salary = salary,

                    testType = comboTest.SelectedItem.ToString()
                };

                // HTTPS + IGNORAR CERTIFICAT
                var handler = new HttpClientHandler();

                handler.ServerCertificateCustomValidationCallback =
                    (message, cert, chain, errors) => true;

                using (HttpClient client = new HttpClient(handler))
                {
                    client.Timeout = TimeSpan.FromSeconds(30);

                    string url =
                        $"{apiBase}?sessionId={PantallaPrincipal.SessionId}";

                    var json = JsonSerializer.Serialize(oferta);

                    var content = new StringContent(
                        json,
                        Encoding.UTF8,
                        "application/json");

                    var response = await client.PostAsync(url, content);

                    var responseText =
                        await response.Content.ReadAsStringAsync();

                    if (response.IsSuccessStatusCode)
                    {
                        MessageBox.Show(
                            "Oferta creada correctament");

                        // LIMPIAR FORMULARI
                        textTitol.Text = "";
                        textDescripcio.Text = "";
                        textHabilitats.Text = "";
                        textUbicacio.Text = "";
                        textSalari.Text = "";

                        comboModalitat.SelectedIndex = -1;
                        comboTipusdecontracte.SelectedIndex = -1;
                        comboCycle.SelectedIndex = -1;
                        comboTest.SelectedIndex = -1;
                    }
                    else
                    {
                        MessageBox.Show(
                            "ERROR BACKEND:\n\n" + responseText);
                    }
                }
            }
            catch (HttpRequestException ex)
            {
                MessageBox.Show(
                    "Error HTTP:\n\n" + ex.Message);
            }
            catch (TaskCanceledException)
            {
                MessageBox.Show(
                    "Timeout del servidor");
            }
            catch (Exception ex)
            {
                MessageBox.Show(
                    "ERROR:\n\n" + ex.Message);
            }
        }

        /// <summary>
        /// Tanca aplicació.
        /// </summary>
        private void btnTancar_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        /// <summary>
        /// Tornar enrere.
        /// </summary>
        private void btnTornar_Click(object sender, EventArgs e)
        {
            _pantallaEmpresa?.Show();

            this.Close();
        }

        private void checkBox2_CheckedChanged(
            object sender,
            EventArgs e)
        {

        }

        private void comboCycle_SelectedIndexChanged(
            object sender,
            EventArgs e)
        {

        }
    }
}