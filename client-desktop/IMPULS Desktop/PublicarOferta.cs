using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Windows.Forms;
/// <summary>
/// <author>Josep Mª</author>
/// Formulari per crear i publicar ofertes de feina.
/// Permet introduir dades, validar-les i enviar-les a l’API.
/// </summary>
namespace IMPULS_Desktop
{
    public partial class PublicarOferta : Form
    {
        private PantallaEmpresa _pantallaEmpresa;

        private readonly string apiBase =
            "http://0bb0dfb7-9b4c-40bc-a0be.5b8c35470a40.bastion.elmeuescriptori.cat:80/offers";

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
        /// Inicialitza els ComboBox del formulari amb les opcions disponibles:
        /// modalitat, tipus de contracte i cicle formatiu.
        /// </summary>
        private void InicializarCombos()
        {
            comboModalitat.Items.Clear();
            comboTipusdecontracte.Items.Clear();
            comboCycle.Items.Clear();

            comboModalitat.Items.AddRange(new string[] { "REMOTE", "HYBRID", "ONSITE" });
            comboTipusdecontracte.Items.AddRange(new string[] { "FP_DUAL", "FCT"});
            comboCycle.Items.AddRange(new string[] { "DAM", "DAW", "ASIX" });
        }
        /// <summary>
        /// Quan es tanca el formulari, es torna a mostrar la pantalla d’empresa.
        /// </summary>
        private void PublicarOferta_FormClosing(object sender, FormClosingEventArgs e)
        {
            _pantallaEmpresa?.Show();
        }

        /// <summary>
        /// Esdeveniment del botó Guardar.
        /// Valida els camps, crea l’objecte oferta i l’envia a l’API.
        /// Si té èxit, neteja el formulari.
        /// </summary>
        private async void btnDesar_Click(object sender, EventArgs e)
        {
            try
            {
                //Validació de camps obligatoris.
                if (string.IsNullOrWhiteSpace(textTitol.Text) ||
                    string.IsNullOrWhiteSpace(textDescripcio.Text) ||
                    string.IsNullOrWhiteSpace(textHabilitats.Text) ||
                    string.IsNullOrWhiteSpace(textUbicacio.Text))
                {
                    MessageBox.Show("Falten camps obligatoris");
                    return;
                }
                //Validació de selecció en els ComboBox.
                if (comboModalitat.SelectedIndex == -1 ||
                    comboTipusdecontracte.SelectedIndex == -1 ||
                    comboCycle.SelectedIndex == -1)
                {
                    MessageBox.Show("Selecciona modalitat, contracte i cicle");
                    return;
                }
                //Validació i conversió del salari.
                decimal salary = 0;
                decimal.TryParse(textSalari.Text, out salary);

                //conversió de skills a llista.
                List<string> skillsList = textHabilitats.Text
                    .Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries)
                    .Select(s => s.Trim())
                    .Where(s => !string.IsNullOrWhiteSpace(s))
                    .ToList();

                //Creació objecte oferta.
                var oferta = new
                {
                    title = textTitol.Text,
                    description = textDescripcio.Text,
                    skills = skillsList,
                    location = textUbicacio.Text,

                    modality = comboModalitat.SelectedItem.ToString(),
                    contractType = comboTipusdecontracte.SelectedItem.ToString(),
                    cycle = comboCycle.SelectedItem.ToString(),

                    salary = salary,

                     hasTest = checkTeProva.Checked
                };

                using (HttpClient client = new HttpClient())
                {
                    string url = $"{apiBase}?sessionId={PantallaPrincipal.SessionId}";

                    var json = JsonSerializer.Serialize(oferta);

                    var content = new StringContent(json, Encoding.UTF8, "application/json");

            

                    var response = await client.PostAsync(url, content);
                    var responseText = await response.Content.ReadAsStringAsync();

             
                    //si es correcte, netejem el formulari.
                    if (response.IsSuccessStatusCode)
                    {
                        MessageBox.Show("Oferta creada correctament");
                        // Limpiar TextBox
                        textTitol.Text = "";
                        textDescripcio.Text = "";
                        textHabilitats.Text = "";
                        textUbicacio.Text = "";
                        textSalari.Text = "";

                        // Resetear ComboBox
                        comboModalitat.SelectedIndex = -1;
                        comboTipusdecontracte.SelectedIndex = -1;
                        comboCycle.SelectedIndex = -1;
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("ERROR:\n" + ex.Message);
            }
        }
        /// <summary>
        /// Tanca completament l’aplicació.
        /// </summary>
        private void btnTancar_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }
        /// <summary>
        /// Torna a la pantalla d’empresa i tanca aquest formulari.
        /// </summary>
        private void btnTornar_Click(object sender, EventArgs e)
        {
            _pantallaEmpresa?.Show();
            this.Close();
        }

        private void checkBox2_CheckedChanged(object sender, EventArgs e)
        {

        }
    }
}