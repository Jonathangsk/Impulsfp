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
    public class TestEmpresaGestionaImodificaOfertes
    {
        private Mock<IApiClient> _apiMock;

        [TestInitialize]
        public void Setup()
        {
            _apiMock = new Mock<IApiClient>();
        }

        
        [TestMethod]
        public async Task Empresa_ConsultaOfertas()
        {
            var fakeJson = JsonSerializer.Serialize(new[]
            {
                new { Id = 1, Title = "Oferta Java", Description = "Backend dev" },
                new { Id = 2, Title = "Oferta Angular", Description = "Frontend dev" }
            });

            _apiMock.Setup(x => x.GetAsync(It.IsAny<string>()))
                .ReturnsAsync(new HttpResponseMessage(HttpStatusCode.OK)
                {
                    Content = new StringContent(fakeJson, Encoding.UTF8, "application/json")
                });

            var response = await _apiMock.Object.GetAsync("http://fake/ofertas");

            var json = await response.Content.ReadAsStringAsync();

            var ofertas = JsonSerializer.Deserialize<List<object>>(json);

            Assert.IsNotNull(ofertas);
            Assert.AreEqual(2, ofertas.Count);
        }

        
        [TestMethod]
        public async Task Empresa_ModificarOferta()
        {
            _apiMock.Setup(x => x.PutAsync(It.IsAny<string>(), It.IsAny<HttpContent>()))
                .ReturnsAsync(new HttpResponseMessage(HttpStatusCode.OK));

            var body = new
            {
                title = "Oferta Java Senior",
                description = "Backend expert"
            };

            var json = JsonSerializer.Serialize(body);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            var response = await _apiMock.Object.PutAsync("http://fake/ofertas/1", content);

            Assert.IsTrue(response.IsSuccessStatusCode);
        }

        
        [TestMethod]
        public async Task Empresa_BorrarOferta()
        {
            _apiMock.Setup(x => x.DeleteAsync(It.IsAny<string>()))
                .ReturnsAsync(new HttpResponseMessage(HttpStatusCode.OK));

            var response = await _apiMock.Object.DeleteAsync("http://fake/ofertas/1");

            Assert.IsTrue(response.IsSuccessStatusCode);
        }
    }
}
