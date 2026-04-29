using System;
using System.Windows.Forms;

namespace IMPULS_Desktop
{
    internal static class Program
    {
        /// <summary>
        /// Punt d'entrada principal de l'aplicació.
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new PantallaPrincipal());
        }
    }
}
