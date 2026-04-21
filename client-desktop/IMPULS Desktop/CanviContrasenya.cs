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
    /// <summary>
    /// Formulari per al canvi de contrasenya.
    /// Permet validar dues contrasenyes i simular el restabliment.
    /// </summary>
    public partial class CanviContrasenya : Form
    {
        /// <summary>
        /// Esdeveniment de càrrega del formulari
        /// </summary>
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
        /// <summary>
        /// Botó per restaurar la contrasenya.
        /// Comprova que les dues contrasenyes coincideixen.
        /// </summary>
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

        /// <summary>
        /// Tanca l’aplicació
        /// </summary>
        private void button1_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        /// <summary>
        /// Torna a la finestra anterior sense guardar canvis
        /// </summary>
        private void button2_Click(object sender, EventArgs e)
        {
            this.Owner.Show(); 
            this.Close();     
        }
    }
    }

