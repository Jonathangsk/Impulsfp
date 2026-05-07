using System.Threading.Tasks;
using System.Windows.Forms;

namespace IMPULS_Desktop.Services
{
    /// <summary>
    /// Servicio d’alertes per a Windows Forms.
    /// Mostra missatges a l’usuari utilitzant MessageBox.
    /// Implementa IAlertService per permetre testing amb mocks.
    /// </summary>
    public class AlertService : IAlertService
    {
        /// <summary>
        /// Mostra un missatge emergent a l’usuari.
        /// </summary>
        /// <param name="titul">Títol de l’alerta</param>
        /// <param name="missatge">Missatge a mostrar</param>
        /// <param name="boto">Text del botó (no utilitzat en WinForms)</param>
        /// <returns>Task completada</returns>

        public Task Mostrar(string titul, string missatge, string boto)
        {
            MessageBox.Show(missatge, titul);
            return Task.CompletedTask;
        }
    }
}