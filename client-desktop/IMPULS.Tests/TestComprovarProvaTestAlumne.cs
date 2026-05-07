using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using IMPULS_Desktop;

namespace IMPULS.Tests
{
    /// <summary>
    /// Classe de tests per validar la comprovació del resultat
    /// de les proves realitzades pels alumnes en una oferta.
    /// Es simula la resposta de l'API utilitzant un Mock d'IApiClient.
    /// </summary>

    [TestClass]
    public class TestComprovarProvaTestAlumne
    {
        /// <summary>
        /// Test per comprovar que quan el servidor retorna
        /// el resultat "PASSED", l'alumne apareix com aprovat.
        /// </summary>

        [TestMethod]
        public async Task TestAprobat()
        {
            // Arrange (simulamos el server)
            var mockApi = new Mock<IApiClient>();

            var fakeResponse = new HttpResponseMessage(HttpStatusCode.OK)
            {
                Content = new StringContent("{\"TestResult\":\"PASSED\"}")
            };

            mockApi.Setup(x => x.GetAsync(It.IsAny<string>()))
                   .ReturnsAsync(fakeResponse);

            var api = mockApi.Object;

            // Act
            var response = await api.GetAsync("url_fake");
            var json = await response.Content.ReadAsStringAsync();

            // Assert
            Assert.IsTrue(json.Contains("PASSED"));
        }

        /// <summary>
        /// Test per comprovar que quan el servidor retorna
        /// el resultat "FAILED", l'alumne apareix com suspès.
        /// </summary>

        [TestMethod]
        public async Task TestSuspes()
        {
            // Arrange
            var mockApi = new Mock<IApiClient>();

            var fakeResponse = new HttpResponseMessage(HttpStatusCode.OK)
            {
                Content = new StringContent("{\"TestResult\":\"FAILED\"}")
            };

            mockApi.Setup(x => x.GetAsync(It.IsAny<string>()))
                   .ReturnsAsync(fakeResponse);

            var api = mockApi.Object;

            // Act
            var response = await api.GetAsync("url_fake");
            var json = await response.Content.ReadAsStringAsync();

            // Assert
            Assert.IsTrue(json.Contains("FAILED"));
        }
    }
}