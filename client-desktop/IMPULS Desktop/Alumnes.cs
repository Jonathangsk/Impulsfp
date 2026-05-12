using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text.Json;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace IMPULS_Desktop
{
    /// <summary>
    /// Formulari que mostra i gestiona els alumnes del sistema.
    /// Permet carregar, visualitzar i eliminar alumnes des de l’API d’administració.
    /// </summary>
    public partial class Alumnes : Form
    {
        // HttpClient global amb bypass de certificat (DEV)
        private static readonly HttpClient client;

        // Base URL sense /auth
        private static readonly string baseUrl;

        static Alumnes()
        {
            var handler = new HttpClientHandler();
            handler.ServerCertificateCustomValidationCallback =
                (message, cert, chain, errors) => true;

            client = new HttpClient(handler);

            baseUrl = PantallaPrincipal.apiBase.Replace("/auth", "");
        }
        /// <summary>
        /// Constructor del formulari Alumnes.
        /// Inicialitza components i assigna l’esdeveniment Load.
        /// </summary>
        public Alumnes()
        {
            InitializeComponent();
            this.Load += Alumnes_Load;
        }
        /// <summary>
        /// Esdeveniment que s’executa quan el formulari es carrega.
        /// Carrega la llista d’alumnes des del servidor.
        /// </summary>
        private async void Alumnes_Load(object sender, EventArgs e)
        {
            await CarregarAlumnes();
        }
        /// <summary>
        /// Carrega els alumnes des de l’API i els mostra al DataGridView.
        /// </summary>
        private async Task CarregarAlumnes()
        {
            try
            {
                // URL de l’endpoint d’administració amb sessionId
                string url =
                    $"{baseUrl}/admin/students?sessionId={PantallaPrincipal.SessionId}";

                // Petició GET al servidor
                var json = await client.GetStringAsync(url);

                // Deserialització del JSON a llista d’objectes Alumne
                var lista = JsonSerializer.Deserialize<List<Alumne>>(
                    json,
                    new JsonSerializerOptions
                    {
                        PropertyNameCaseInsensitive = true
                    });

                dataGridView1.AutoGenerateColumns = true;
                dataGridView1.DataSource = lista;

                dataGridView1.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
                dataGridView1.MultiSelect = false;
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error al carregar alumnes: " + ex.Message);
            }
        }
        /// <summary>
        /// Elimina l’alumne seleccionat al DataGridView.
        /// Fa una petició DELETE a l’API d’administració.
        /// </summary>
        private async void btnEliminarCandidat_Click(object sender, EventArgs e)
        {
            // Comprova si hi ha selecció
            if (dataGridView1.SelectedRows.Count == 0)
            {
                MessageBox.Show("Selecciona un alumne");
                return;
            }

            var alumne = dataGridView1.SelectedRows[0].DataBoundItem as Alumne;

            if (alumne == null) return;

            var confirm = MessageBox.Show(
                "Segur que vols eliminar aquest alumne?",
                "Confirmar",
                MessageBoxButtons.YesNo,
                MessageBoxIcon.Warning
            );

            if (confirm == DialogResult.Yes)
            {
                try
                {
                    // Endpoint DELETE amb sessionId

                    string url =
                        $"{baseUrl}/admin/students/{alumne.Id}?sessionId={PantallaPrincipal.SessionId}";

                    var response = await client.DeleteAsync(url);

                    if (response.IsSuccessStatusCode)
                    {
                        MessageBox.Show("Alumne eliminat correctament");
                        await CarregarAlumnes();
                    }
                    else
                    {
                        string error = await response.Content.ReadAsStringAsync();
                        MessageBox.Show("Error: " + error);
                    }
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Error al eliminar: " + ex.Message);
                }
            }
        }
        /// <summary>
        /// Tanca el formulari actual i torna a la pantalla anterior.
        /// </summary>
        private void btnTornar_Click(object sender, EventArgs e)
        {
            this.Close();
        }
        /// <summary>
        /// Botó per tancar l’aplicació
        /// </summary>
        private void btnTancar_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }
    }
}