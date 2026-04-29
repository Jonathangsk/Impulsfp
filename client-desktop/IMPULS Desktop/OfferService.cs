using System.Net.Http;
using System.Threading.Tasks;

namespace IMPULS_Desktop
{
    /// <summary>
    /// Servei intern per gestionar ofertes via API.
    /// </summary>
    internal class OfferService
    {
        private readonly HttpClient _client;

        public OfferService()
        {
            _client = new HttpClient();
        }

        /// <summary>
        /// Elimina una oferta per ID.
        /// </summary>
        public async Task DeleteOffer(int id, string sessionId)
        {
            var url =
                $"http://0bb0dfb7-9b4c-40bc-a0be.5b8c35470a40.bastion.elmeuescriptori.cat/offers/{id}?sessionId={sessionId}";

            var response = await _client.DeleteAsync(url);

            response.EnsureSuccessStatusCode();
        }
    }
}

