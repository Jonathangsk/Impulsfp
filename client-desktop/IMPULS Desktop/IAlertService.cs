using System.Threading.Tasks;

namespace IMPULS_Desktop.Services
{
    /// <summary>
    /// Servei d’alertes per mostrar missatges a l’usuari.
    /// Permet desacoblar la UI per facilitar testing.
    /// </summary>
    public interface IAlertService

    {
        /// <summary>
        /// Mostra una alerta a l’usuari.
        /// </summary>
        /// <param name="titol">Títol de l’alerta.</param>
        /// <param name="missatge">Missatge principal.</param>
        /// <param name="boto">Text del botó de confirmació.</param>
        Task Mostrar(string titol, string missatge, string boto);
    }
}