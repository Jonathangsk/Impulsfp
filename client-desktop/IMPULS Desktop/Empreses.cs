using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text.Json;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace IMPULS_Desktop
{
    public partial class Empreses : Form
    {
        private static readonly HttpClient client = new HttpClient();

        public Empreses()
        {
            InitializeComponent();
        }

        private async void Empreses_Load(object sender, EventArgs e)
        {
            await CarregarEmpreses();

            dataGridViewEmpreses.SelectionChanged += dataGridViewEmpresas_SelectionChanged;
        }

        //Carreguem empreses
        private async Task CarregarEmpreses()
        {
            try
            {
                var json = await client.GetStringAsync($"{PantallaPrincipal.apiBase}/company");

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
                MessageBox.Show("Error al carregar empreses: " + ex.Message);
            }
        }

        // Cuan seleccionem una empresa es carreguen les ofertes
        private void dataGridViewEmpresas_SelectionChanged(object sender, EventArgs e)
        {
            try
            {
                var empresa = dataGridViewEmpreses.CurrentRow?.DataBoundItem as Empresa;

                if (empresa != null)
                {
                    dataGridViewOfertes.AutoGenerateColumns = true;
                    dataGridViewOfertes.DataSource = empresa.ActiveOffers;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error al carregar ofertes: " + ex.Message);
            }
        }

        //Editem la empresa
        private async void dataGridViewEmpresas_CellEndEdit(object sender, DataGridViewCellEventArgs e)
        {
            try
            {
                var empresa = dataGridViewEmpreses.CurrentRow?.DataBoundItem as Empresa;
                if (empresa == null) return;

                var json = JsonSerializer.Serialize(empresa);
                var content = new StringContent(json, System.Text.Encoding.UTF8, "application/json");

                await client.PutAsync(
                    $"{PantallaPrincipal.apiBase}/company/{empresa.Id}",
                    content
                );
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error al desar empresa: " + ex.Message);
            }
        }

        // Eliminem Empresa
        private async void btnEliminarEmpresa_Click(object sender, EventArgs e)
        {
            var empresa = dataGridViewEmpreses.CurrentRow?.DataBoundItem as Empresa;

            if (empresa == null)
            {
                MessageBox.Show("Selecciona una empresa");
                return;
            }

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
                    await client.DeleteAsync(
                        $"{PantallaPrincipal.apiBase}/company/{empresa.Id}"
                    );

                    await CarregarEmpreses();
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Error al eliminar: " + ex.Message);
                }
            }
        }

        private void btnTornar_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void btnTancar_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        private void btnEliminarCandidat_Click(object sender, EventArgs e)
        {

        }
    }
}
