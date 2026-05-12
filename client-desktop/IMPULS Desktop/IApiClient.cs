using System.Net.Http;
using System.Threading.Tasks;

namespace IMPULS_Desktop
{
    /// <summary>
    /// Abstracció del client HTTP per accedir a l’API.
    /// Permet desacoblar HttpClient per facilitar testing i manteniment.
    /// </summary>
    public interface IApiClient
    {
        /// <summary>
        /// Realitza una petició HTTP GET.
        /// </summary>
        /// <param name="url">URL de l’endpoint.</param>

        Task<HttpResponseMessage> GetAsync(string url);
        /// <summary>
        /// Realitza una petició HTTP PUT.
        /// </summary>
        /// <param name="url">URL de l’endpoint.</param>
        /// <param name="content">Contingut de la petició.</param>
        Task<HttpResponseMessage> PutAsync(string url, HttpContent content);
        /// <summary>
        /// Realitza una petició HTTP DELETE.
        /// </summary>
        /// <param name="url">URL de l’endpoint.</param>
        Task<HttpResponseMessage> DeleteAsync(string url);
    }
}