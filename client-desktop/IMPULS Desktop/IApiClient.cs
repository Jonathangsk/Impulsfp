using System.Net.Http;
using System.Threading.Tasks;

namespace IMPULS_Desktop
{
    public interface IApiClient
    {
        Task<HttpResponseMessage> GetAsync(string url);
        Task<HttpResponseMessage> PutAsync(string url, HttpContent content);
        Task<HttpResponseMessage> DeleteAsync(string url);
    }
}