using System.Net.Http;
using System.Threading.Tasks;

namespace IMPULS_Desktop
{
    public class ApiClient : IApiClient
    {
        private readonly HttpClient _client = new HttpClient();

        public Task<HttpResponseMessage> GetAsync(string url)
        {
            return _client.GetAsync(url);
        }

        public Task<HttpResponseMessage> PutAsync(string url, HttpContent content)
        {
            return _client.PutAsync(url, content);
        }

        public Task<HttpResponseMessage> DeleteAsync(string url)
        {
            return _client.DeleteAsync(url);
        }
    }
}