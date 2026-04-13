using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using static System.Windows.Forms.VisualStyles.VisualStyleElement;

namespace IMPULS_Desktop
{
    public partial class CanviContrasenya : Form
    {
        public CanviContrasenya()
        {
            InitializeComponent();
        }

        private void CanviContrasenya_Load(object sender, EventArgs e)
        {

        }

        private void label3_Click(object sender, EventArgs e)
        {

        }

        private void textBox2_TextChanged(object sender, EventArgs e)
        {

        }

        private void textBox4_TextChanged(object sender, EventArgs e)
        {

        }

        private void restaurar_Click(object sender, EventArgs e)
        {
         
            if (textBox2.Text != textBox4.Text)
       
            {
                MessageBox.Show("Les contrasenyes han de ser iguals");
                return;
            }

            MessageBox.Show("Contrasenya modificada correctament");
            this.Owner?.Show();
            this.Hide();
          
        }

        private void button1_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            this.Owner.Show(); 
            this.Close();     
        }
    }
    }

