using System;
using System.Windows.Forms;
using Npgsql;

namespace IMPULS_Desktop
{
    public partial class Form1 : Form
    {


        private string connectionString = "Host=localhost;Port=5432;Username=postgres;Password=jose;Database=probando";
        public Form1()
        {
            InitializeComponent();
            this.Load += Form1_Load;
        }

        private void Form1_Load(object sender, EventArgs e)
        {
          
        }

        private bool ValidarUsuario(string usuari, string contrasenya, out string tipus)
        {
            tipus = "";
            try
            {
                using (var conn = new NpgsqlConnection(connectionString))
                {
                    conn.Open();

                    string sql = "SELECT tipo FROM usuarios WHERE usuario = @usuario AND contraseña = @contraseña";
                    using (var cmd = new NpgsqlCommand(sql, conn))
                    {
                        cmd.Parameters.AddWithValue("usuario", usuari);
                        cmd.Parameters.AddWithValue("contraseña", contrasenya);

                        var result = cmd.ExecuteScalar();
                        if (result != null)
                        {
                            tipus = result.ToString();
                            return true;
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error en connectar amb la base de dades: " + ex.Message);
            }

            return false;
        }


        private void button1_Click(object sender, EventArgs e)
        {

            string usuari = textBoxUsuario.Text;
            string contrasenya = textBoxContraseña.Text;

            if (ValidarUsuario(usuari, contrasenya, out string tipus))
            {
                MessageBox.Show($"Inici de sessió correcte. Tipus d'usuari: {tipus}");
                if (tipus == "admin")
                {
                    Form2 formAdmin = new Form2();
                    formAdmin.Show();


                }
                else if (tipus == "empresa")
                {

                }
            }
            else
            {
                MessageBox.Show("Usuari o contrasenya incorrectes");
            }
        }


        private void textBox3_TextChanged(object sender, EventArgs e) { }
        private void pictureBox1_Click(object sender, EventArgs e) { }
        private void textBox1_TextChanged(object sender, EventArgs e) { }
        private void label4_Click(object sender, EventArgs e) { }
    }
}