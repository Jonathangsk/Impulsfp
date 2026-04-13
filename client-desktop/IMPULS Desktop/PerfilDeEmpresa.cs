using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace IMPULS_Desktop
{
    public partial class PerfilDeEmpresa : Form
    {
        public PerfilDeEmpresa()
        {
            InitializeComponent();

            this.Load += async (s, e) => await CargarDatos();
            dataGridView1.CellEndEdit += dataGridView1_CellEndEdit;
        }


        private async Task CargarDatos()
        {
            List<Empresa> lista = new List<Empresa>(); 

            try
            {
                using (HttpClient client = new HttpClient())
                {
                    client.Timeout = TimeSpan.FromSeconds(5);

                    var json = await client.GetStringAsync("http://localhost:8080/empresa");

                    var empresa = JsonSerializer.Deserialize<Empresa>(
                        json,
                        new JsonSerializerOptions
                        {
                            PropertyNameCaseInsensitive = true
                        }
                    );

                    if (empresa != null)
                    {
                        lista.Add(empresa);
                    }
                }
            }
            catch (HttpRequestException)
            {
                MessageBox.Show("Error de connexió amb el servidor.");
            }
            catch (TaskCanceledException)
            {
                MessageBox.Show("El servidor triga molt en respondre.");
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error inesperat: " + ex.Message);
            }

            
            dataGridView1.AutoGenerateColumns = true;
            dataGridView1.DataSource = lista;

            dataGridView1.ReadOnly = false;
            dataGridView1.AllowUserToAddRows = false;
            dataGridView1.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
        }

        private async void dataGridView1_CellEndEdit(object sender, DataGridViewCellEventArgs e)
        {
            try
            {
                var fila = dataGridView1.Rows[e.RowIndex];

                var empresa = new Empresa
                {
                    Id = Convert.ToInt32(fila.Cells["id"].Value),
                    Name = fila.Cells["nombre"].Value?.ToString(),
                    Email = fila.Cells["email"].Value?.ToString()
                };

                using (HttpClient client = new HttpClient())
                {
                    var json = JsonSerializer.Serialize(empresa);
                    var content = new StringContent(json, Encoding.UTF8, "application/json");

                    var response = await client.PutAsync("http://localhost:8080/empresa", content);

                    if (!response.IsSuccessStatusCode)
                    {
                        MessageBox.Show("Error en guardar els canvis");
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error: " + ex.Message);
            }
        }

      
        private async void btnEliminarCandidat_Click(object sender, EventArgs e)
        {
            try
            {
                if (dataGridView1.CurrentRow == null)
                {
                    MessageBox.Show("Selecciona una fila.");
                    return;
                }

                int id = Convert.ToInt32(dataGridView1.CurrentRow.Cells["id"].Value);

                using (HttpClient client = new HttpClient())
                {
                    var response = await client.DeleteAsync($"http://localhost:8080/empresa/{id}");

                    if (response.IsSuccessStatusCode)
                    {
                        MessageBox.Show("Eliminat correctament");
                        await CargarDatos();
                    }
                    else
                    {
                        MessageBox.Show("Error al eliminar");
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error: " + ex.Message);
            }
        }

      
        private void btnTancar_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        private void btnTornar_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void PerfilDeEmpresa_Load(object sender, EventArgs e)
        {

        }

  
    }
}