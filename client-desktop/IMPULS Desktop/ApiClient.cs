using System.Net.Http;
using System.Threading.Tasks;

namespace IMPULS_Desktop
{
    /// <summary>
    /// Client HTTP per comunicar-se amb l’API.
    /// Encapsula HttpClient per facilitar testing i desacoblament.
    /// </summary>
    public class ApiClient : IApiClient
    {
        private readonly HttpClient _client = new HttpClient();

        /// <summary>
        /// Realitza una petició GET a l’API.
        /// </summary>
        public Task<HttpResponseMessage> GetAsync(string url)
        {
            return _client.GetAsync(url);
        }

        /// <summary>
        /// Realitza una petició PUT a l’API.
        /// </summary>
        public Task<HttpResponseMessage> PutAsync(string url, HttpContent content)
        {
            return _client.PutAsync(url, content);
        }
        /// <summary>
        /// Realitza una petició DELETE a l’API.
        /// </summary>

        public Task<HttpResponseMessage> DeleteAsync(string url)
        {
            return _client.DeleteAsync(url);
        }
    }
}