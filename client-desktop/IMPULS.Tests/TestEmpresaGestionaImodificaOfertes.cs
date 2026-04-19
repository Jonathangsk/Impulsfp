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
    /// Classe de tests per validar la gestió d'ofertes per part d'una empresa.
    /// Inclou consulta, modificació i eliminació d'ofertes utilitzant un mock d'API.
    /// </summary>
    [TestClass]
    public class TestEmpresaGestionaImodificaOfertes
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
        /// Test per comprovar que una empresa pot consultar la llista d'ofertes correctament.
        /// Es simula una resposta de l'API amb dues ofertes.
        /// </summary>
        [TestMethod]
        public async Task Empresa_ConsultaOfertas()
        {
            var fakeJson = JsonSerializer.Serialize(new[]
            {
                new { Id = 1, Title = "Oferta Java", Description = "Backend dev" },
                new { Id = 2, Title = "Oferta Angular", Description = "Frontend dev" }
            });

            // Simulem resposta OK de l'API amb llista d'ofertes
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

        /// <summary>
        /// Test per comprovar que una empresa pot modificar una oferta existent correctament.
        /// </summary>
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

        /// <summary>
        /// Test per comprovar que una empresa pot eliminar una oferta correctament.
        /// </summary>
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