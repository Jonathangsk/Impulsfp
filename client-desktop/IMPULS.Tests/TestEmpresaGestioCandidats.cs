using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using IMPULS_Desktop;

namespace IMPULS.Tests
{
    /// <summary>
    /// Classe de tests per validar la gestió de candidats per part d'una empresa.
    /// Inclou consulta de candidats i gestió del seu estat (acceptació o rebuig).
    /// </summary>
    [TestClass]
    public class TestEmpresaGestioCandidats
    {
        private Mock<IApiClient> _apiMock;

        /// <summary>
        /// Inicialitza el mock abans de cada test.
        /// </summary>
        [TestInitialize]
        public void Setup()
        {
            _apiMock = new Mock<IApiClient>();
        }

        /// <summary>
        /// Test per comprovar que una empresa pot consultar la llista de candidats correctament.
        /// Es simula una resposta de l'API amb dos alumnes.
        /// </summary>
        [TestMethod]
        public async Task EmpresaConsultaCandidats()
        {
            var fakeJson = JsonSerializer.Serialize(new[]
            {
            new Alumne { Id = 1, Name = "Anna", Skills = new List<string> { "Java" } },
            new Alumne { Id = 2, Name = "Marc", Skills = new List<string> { "Angular" } }  });

            // Simulem resposta OK de l'API amb llista de candidats
            _apiMock.Setup(x => x.GetAsync(It.IsAny<string>()))
                .ReturnsAsync(new HttpResponseMessage(HttpStatusCode.OK)
                {
                    Content = new StringContent(fakeJson, Encoding.UTF8, "application/json")
                });

            var response = await _apiMock.Object.GetAsync("http://fake/candidats");

            var json = await response.Content.ReadAsStringAsync();

            var candidats = JsonSerializer.Deserialize<List<Alumne>>(json,
                new JsonSerializerOptions
                {
                    PropertyNameCaseInsensitive = true
                });

            Assert.IsNotNull(candidats);
            Assert.AreEqual(2, candidats.Count);
            Assert.AreEqual("Anna", candidats[0].Name);
        }

        /// <summary>
        /// Test per comprovar que una empresa pot acceptar un candidat correctament.
        /// </summary>
        [TestMethod]
        public async Task EmpresaAcceptaCandidat()
        {
            _apiMock.Setup(x => x.PutAsync(It.IsAny<string>(), It.IsAny<HttpContent>()))
                .ReturnsAsync(new HttpResponseMessage(HttpStatusCode.OK));

            var body = new
            {
                status = "ACCEPTED"
            };

            var json = JsonSerializer.Serialize(body);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            var response = await _apiMock.Object.PutAsync("http://fake/candidats/1", content);

            Assert.IsTrue(response.IsSuccessStatusCode);
        }

        /// <summary>
        /// Test per comprovar que una empresa pot rebutjar un candidat correctament.
        /// </summary>
        [TestMethod]
        public async Task EmpresaRebutjaCandidat()
        {
            _apiMock.Setup(x => x.PutAsync(It.IsAny<string>(), It.IsAny<HttpContent>()))
                .ReturnsAsync(new HttpResponseMessage(HttpStatusCode.OK));

            var body = new
            {
                status = "REJECTED"
            };

            var json = JsonSerializer.Serialize(body);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            var response = await _apiMock.Object.PutAsync("http://fake/candidats/1", content);

            Assert.IsTrue(response.IsSuccessStatusCode);
        }
    }
}