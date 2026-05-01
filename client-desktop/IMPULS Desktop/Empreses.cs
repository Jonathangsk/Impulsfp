using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text.Json;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace IMPULS_Desktop
{
    /// <summary>
    /// Formulari que mostra les empreses i les seves ofertes.
    /// Permet visualitzar, filtrar i eliminar empreses o ofertes.
    /// </summary>
    public partial class Empreses : Form
    {
        private static readonly HttpClient client = new HttpClient();

        /// <summary>
        /// Constructor del formulari Empreses.
        /// Inicialitza els components i assigna els events principals.
        /// </summary>
        public Empreses()
        {
            InitializeComponent();
            this.Load += Empreses_Load;
            dataGridViewEmpreses.SelectionChanged += dataGridViewEmpreses_SelectionChanged;
        }

        /// <summary>
        /// Esdeveniment Load del formulari.
        /// Carrega les empreses des de l’API.
        /// </summary>
        private async void Empreses_Load(object sender, EventArgs e)
        {
            await CarregarEmpreses();

            dataGridViewEmpreses.SelectionChanged += dataGridViewEmpreses_SelectionChanged;
        }

        /// <summary>
        /// Carrega les empreses des del servidor i les mostra al DataGridView.
        /// </summary>
        
        private async Task CarregarEmpreses()
        {
            try
            {
                string baseUrl = PantallaPrincipal.apiBase.Replace("/auth", "");

                string url = $"{baseUrl}/admin/companies?sessionId={PantallaPrincipal.SessionId}";

                var json = await client.GetStringAsync(url);

                var lista = JsonSerializer.Deserialize<List<Empresa>>(
                    json,
                    new JsonSerializerOptions
                    {
                        PropertyNameCaseInsensitive = true
                    });

                dataGridViewEmpreses.AutoGenerateColumns = true;
                dataGridViewEmpreses.DataSource = lista;

                dataGridViewEmpreses.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
                dataGridViewEmpreses.MultiSelect = false;
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.ToString());
            }
        }

        /// <summary>
        /// Esdeveniment que es dispara quan es selecciona una empresa.
        /// Carrega les ofertes associades a l’empresa seleccionada.
        /// </summary>
        private async void dataGridViewEmpreses_SelectionChanged(object sender, EventArgs e)
        {
            
            try
            {
                var empresa = dataGridViewEmpreses.CurrentRow?.DataBoundItem as Empresa;

                if (empresa == null)
                    return;

                string baseUrl = PantallaPrincipal.apiBase.Replace("/auth", "");
                string url = $"{baseUrl}/admin/offers?sessionId={PantallaPrincipal.SessionId}";

                string json = await client.GetStringAsync(url);
                
                var offers = JsonSerializer.Deserialize<List<Oferta>>(
                    json,
                    new JsonSerializerOptions
                    {
                        PropertyNameCaseInsensitive = true
                    });

                var filtered = offers
     .Where(o => o.CompanyName != null &&
                 empresa.Name != null &&
                 o.CompanyName.Trim().ToLower() == empresa.Name.Trim().ToLower())
     .ToList();

                dataGridViewOfertes.AutoGenerateColumns = true;
                dataGridViewOfertes.DataSource = filtered;
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error al carregar ofertes: " + ex.Message);
            }
        }
        /// <summary>
        /// Tanca el formulari actual i torna enrere.
        /// </summary>
        private void btnTornar_Click(object sender, EventArgs e)
        {
            this.Close();
        }
        /// <summary>
        /// Tanca completament l’aplicació.
        /// </summary>
        private void btnTancar_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        /// <summary>
        /// Elimina una oferta seleccionada o, si no n’hi ha, elimina una empresa.
        /// </summary>
        private async void btnEliminar_Click(object sender, EventArgs e)
        {
            string baseUrl = PantallaPrincipal.apiBase.Replace("/auth", "");

            // si hi ha ofertes seleccionades, borrem la oferta
            if (dataGridViewOfertes.SelectedRows.Count > 0)
            {
                var oferta = dataGridViewOfertes.SelectedRows[0].DataBoundItem as Oferta;

                if (oferta == null) return;

                var confirm = MessageBox.Show(
                    "Segur que vols eliminar aquesta oferta?",
                    "Confirmar",
                    MessageBoxButtons.YesNo,
                    MessageBoxIcon.Warning
                );

                if (confirm == DialogResult.Yes)
                {
                    try
                    {
                        string url = $"{baseUrl}/admin/offers/{oferta.Id}?sessionId={PantallaPrincipal.SessionId}";

                        var response = await client.DeleteAsync(url);

                        if (response.IsSuccessStatusCode)
                        {
                            MessageBox.Show("Oferta eliminada correctament");

                            // actualitzem ofertes d'aquesta finestra
                            dataGridViewEmpreses_SelectionChanged(null, null);
                        }
                        else
                        {
                            string error = await response.Content.ReadAsStringAsync();
                            MessageBox.Show("Error: " + error);
                        }
                    }
                    catch (Exception ex)
                    {
                        MessageBox.Show("Error al eliminar oferta: " + ex.Message);
                    }
                }

                return; 
            }

            // si no hi han ofertes, borrem empresa
            if (dataGridViewEmpreses.SelectedRows.Count > 0)
            {
                var empresa = dataGridViewEmpreses.SelectedRows[0].DataBoundItem as Empresa;

                if (empresa == null) return;

                var confirm = MessageBox.Show(
                    "Segur que vols eliminar aquesta empresa?",
                    "Confirmar",
                    MessageBoxButtons.YesNo,
                    MessageBoxIcon.Warning
                );

                if (confirm == DialogResult.Yes)
                {
                    try
                    {
                        string url = $"{baseUrl}/admin/companies/{empresa.Id}?sessionId={PantallaPrincipal.SessionId}";

                        var response = await client.DeleteAsync(url);

                        if (response.IsSuccessStatusCode)
                        {
                            MessageBox.Show("Empresa eliminada correctament");

                            await CarregarEmpreses();
                            dataGridViewOfertes.DataSource = null; // limpiar ofertas
                        }
                        else
                        {
                            string error = await response.Content.ReadAsStringAsync();
                            MessageBox.Show("Error: " + error);
                        }
                    }
                    catch (Exception ex)
                    {
                        MessageBox.Show("Error al eliminar empresa: " + ex.Message);
                    }
                }
            }
            else
            {
                MessageBox.Show("Selecciona una empresa o una oferta");
            }
        
    }

        private void label1_Click(object sender, EventArgs e)
        {

        }
    }
}
