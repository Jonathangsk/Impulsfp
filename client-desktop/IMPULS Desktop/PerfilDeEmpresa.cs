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
        private readonly IApiClient _api = new ApiClient();

        private PantallaEmpresa _pantallaEmpresa;

     
        private void PerfilEmpresa_FormClosing(object sender, FormClosingEventArgs e)
        {
            _pantallaEmpresa.Show();
        }
        public PerfilDeEmpresa(PantallaEmpresa pantallaEmpresa)
        {
            InitializeComponent();

            _pantallaEmpresa = pantallaEmpresa;
            this.FormClosing += PerfilEmpresa_FormClosing;
            this.Load += async (s, e) => await CarregarDades();

            dataGridView1.CellEndEdit += dataGridView1_CellEndEdit;
            dataGridView2.CellEndEdit += dataGridView2_CellEndEdit;
        }
        private Empresa empresaActual;
        private bool _guardat = false;
        private async Task CarregarDades()
        {
            List<Empresa> lista = new List<Empresa>();

            try
            {
                using (HttpClient client = new HttpClient())
                {
                    client.Timeout = TimeSpan.FromSeconds(10);

                    string url =
                        $"http://0bb0dfb7-9b4c-40bc-a0be.5b8c35470a40.bastion.elmeuescriptori.cat:80/users/me?sessionId={PantallaPrincipal.SessionId}";

                    var json = await client.GetStringAsync(url);

             

                    // Deserelitzem
                    var empresa = JsonSerializer.Deserialize<Empresa>(
                        json,
                        new JsonSerializerOptions
                        {
                            PropertyNameCaseInsensitive = true
                        });

                    if (empresa != null)
                    {
                        empresaActual = empresa;

                        //  tecnologíes
                        empresa.TechnologiesText =
                            string.Join(", ", empresa.Technologies ?? new List<string>());

                        lista.Add(empresa);

                  
                    }
                }

                // Empresa
                dataGridView1.AutoGenerateColumns = true;
                dataGridView1.DataSource = lista;
                dataGridView1.ReadOnly = false;
                dataGridView1.AllowUserToAddRows = false;
                dataGridView1.SelectionMode = DataGridViewSelectionMode.FullRowSelect;

                
                if (dataGridView1.Columns.Contains("Technologies"))
                    dataGridView1.Columns["Technologies"].Visible = false;

                if (dataGridView1.Columns.Contains("Password"))
                    dataGridView1.Columns["Password"].Visible = false;

                // Ofertes
                dataGridView2.AutoGenerateColumns = true;

               
                dataGridView2.DataSource = new List<Oferta>();

                dataGridView2.ReadOnly = false;
                dataGridView2.AllowUserToAddRows = false;
                dataGridView2.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
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
        }
       
  


        private async void dataGridView1_CellEndEdit(object sender, DataGridViewCellEventArgs e)
        {
            try
            {
                if (empresaActual == null) return;


                var columnName = dataGridView1.Columns[e.ColumnIndex].Name;

                if (columnName == "Id" ||
                    columnName == "Email" ||
                    columnName == "VatNumber" ||
                    columnName == "Username" ||
                    columnName == "ActiveOffers")
                {
                    MessageBox.Show("Aquest camp no es pot editar");
                    await CarregarDades();
                    return;
                }

                var body = new
                {
                    name = empresaActual.Name,
                    address = empresaActual.Address,
                    phone = empresaActual.Phone,
                    website = empresaActual.Website,
                    niche = empresaActual.Niche,
                    technologies = empresaActual.Technologies ?? new List<string>()

                };
         
        
      
                
                string url =
                    $"http://0bb0dfb7-9b4c-40bc-a0be.5b8c35470a40.bastion.elmeuescriptori.cat:80/users/me?sessionId={PantallaPrincipal.SessionId}";

                using (HttpClient client = new HttpClient())
                {
                    var json = JsonSerializer.Serialize(body);
                    
                    var content = new StringContent(json, Encoding.UTF8, "application/json");

                    var response = await client.PutAsync(url, content);

                    var responseText = await response.Content.ReadAsStringAsync();

                    if (response.IsSuccessStatusCode)
                    {
                        MessageBox.Show("Dades Guardades correctament");
                        await CarregarDades();
                    }
                    else
                    {
                        MessageBox.Show("ERROR BACKEND:\n" + responseText);
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


        
        private async void dataGridView2_CellEndEdit(object sender, DataGridViewCellEventArgs e)
        {
            try
            {
                if (e.RowIndex < 0) return;

                var fila = dataGridView2.Rows[e.RowIndex];

                var oferta = new Oferta
                {
                    Id = Convert.ToInt32(fila.Cells["id"].Value),
                    Title = fila.Cells["title"].Value?.ToString(),
                    Description = fila.Cells["description"].Value?.ToString()
                };

                using (HttpClient client = new HttpClient())
                {
                    var json = JsonSerializer.Serialize(oferta);
                    var content = new StringContent(json, Encoding.UTF8, "application/json");

                    var response = await client.PutAsync($"{PantallaPrincipal.apiBase}/ofertas", content);

                    if (!response.IsSuccessStatusCode)
                    {
                        MessageBox.Show("Error al guardar l'oferta");
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error: " + ex.Message);
            }
        }
        private async void btnEliminar_Click(object sender, EventArgs e)
        {
            try
            {
                // Ofertes (grid2)
                if (dataGridView2.CurrentRow != null)
                {
                    int idOferta = Convert.ToInt32(dataGridView2.CurrentRow.Cells["id"].Value);

                    using (HttpClient client = new HttpClient())
                    {
                        var response = await client.DeleteAsync($"{PantallaPrincipal.apiBase}/ofertas/{idOferta}");

                        if (response.IsSuccessStatusCode)
                        {
                            MessageBox.Show("Oferta eliminada");
                           await CarregarDades();
                        }
                        else
                        {
                            MessageBox.Show("Error al eliminar l'oferta");
                        }
                    }

                    return;
                }

                // si no hi ha oferta, mirem empresa
                if (dataGridView1.CurrentRow != null)
                {
                    int idEmpresa = Convert.ToInt32(dataGridView1.CurrentRow.Cells["id"].Value);

                    using (HttpClient client = new HttpClient())
                    {
                        var response = await client.DeleteAsync($"{PantallaPrincipal.apiBase}/empresa/{idEmpresa}");

                        if (response.IsSuccessStatusCode)
                        {
                            MessageBox.Show("Empresa eliminada");
                            await CarregarDades();
                        }
                        else
                        {
                            MessageBox.Show("Error al eliminar l'empresa");
                        }
                    }

                    return;
                }

                MessageBox.Show("Selecciona per eliminar");
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error: " + ex.Message);
            }
        }

        private void btnTornar_Click_1(object sender, EventArgs e)
        {
            _pantallaEmpresa.Show();
            this.Close();
        }

        private void btnTancar_Click_1(object sender, EventArgs e)
        {
            Application.Exit();
        }

        private void PerfilDeEmpresa_Load(object sender, EventArgs e)
        {

        }
    }
}