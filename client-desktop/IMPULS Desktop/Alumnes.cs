using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text.Json;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace IMPULS_Desktop
{
    public partial class Alumnes : Form
    {
        
        private static readonly HttpClient client = new HttpClient();

        public Alumnes()
        {
            InitializeComponent();
        }

        private async void Alumnes_Load(object sender, EventArgs e)
        {
            await CarregarAlumnes();
        }

        
        private async Task CarregarAlumnes()
        {
            try
            {
                var json = await client.GetStringAsync($"{PantallaPrincipal.apiBase}/alumnes");

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

        // Editem i guardem automaticament
        private async void dataGridView1_CellEndEdit(object sender, DataGridViewCellEventArgs e)
        {
            try
            {
                var alumne = dataGridView1.CurrentRow?.DataBoundItem as Alumne;
                if (alumne == null) return;

                var json = JsonSerializer.Serialize(alumne);
                var content = new StringContent(json, System.Text.Encoding.UTF8, "application/json");

                await client.PutAsync(
                    $"{PantallaPrincipal.apiBase}/alumnes/{alumne.Id}",
                    content
                );
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error al guardar alumne: " + ex.Message);
            }
        }

        
        private async void btnEliminarCandidat_Click(object sender, EventArgs e)
        {
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
                    await client.DeleteAsync(
                        $"{PantallaPrincipal.apiBase}/student/{alumne.Id}"
                    );

                    await CarregarAlumnes(); //Refresquem la llista
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

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            
        }
    }
}