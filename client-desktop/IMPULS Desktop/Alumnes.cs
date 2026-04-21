using System;
using System.Net.Http;
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
            this.Load += Alumnes_Load;
        }

        private async void Alumnes_Load(object sender, EventArgs e)
        {
            await CarregarAlumnes();
        }

        
        private async Task CarregarAlumnes()
        {
         
        }

       
        private async void dataGridView1_CellEndEdit(object sender, DataGridViewCellEventArgs e)
        {
        }

        
        private async void btnEliminarCandidat_Click(object sender, EventArgs e)
        {
            
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