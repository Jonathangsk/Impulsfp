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
    [TestClass]
    public class TestEmpresaMiraIgestionaCandidats
    {
        private Mock<IApiClient> _apiMock;

        [TestInitialize]
        public void Setup()
        {
            _apiMock = new Mock<IApiClient>();
        }

        
        [TestMethod]
        public async Task Empresa_ConsultaCandidats()
        {
            var fakeJson = JsonSerializer.Serialize(new[]
            {
                new Alumne { Id = 1, Name = "Anna", Skills = "Java" },
                new Alumne { Id = 2, Name = "Marc", Skills = "Angular" }
            });

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

        
        [TestMethod]
        public async Task Empresa_AcceptaCandidat()
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

        
        [TestMethod]
        public async Task Empresa_RebutjaCandidats()
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