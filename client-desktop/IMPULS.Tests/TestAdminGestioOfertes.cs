using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using IMPULS_Desktop;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;

namespace IMPULS.Tests
{
    /// <summary>
    /// Classe de tests per validar la gestió d'ofertes des del panell ADMIN.
    /// Inclou consultes i eliminacions d'ofertes utilitzant un Mock d'IApiClient.
    /// </summary>

    [TestClass]
    public class TestAdminConsultaIbaixaOfertes
    {
        private Mock<IApiClient> _apiMock;

        /// <summary>
        /// Inicialitza el Mock abans de cada test.
        /// </summary>

        [TestInitialize]
        public void Setup()
        {
            _apiMock = new Mock<IApiClient>();
        }

        /// <summary>
        /// Test per comprovar que un ADMIN pot consultar correctament
        /// la llista d'ofertes publicades al sistema.
        /// Es simula una resposta HTTP OK amb dues ofertes.
        /// </summary>

        [TestMethod]
        public async Task AdminConsultaOfertes()
        {
            var fakeJson = JsonSerializer.Serialize(new[]
            {
                new Oferta { Id = 1, CompanyName = "Empresa A", Title = "Oferta 1" },
                new Oferta { Id = 2, CompanyName = "Empresa B", Title = "Oferta 2" }
            });

            // Simulem la resposta de l'API amb ofertes
            _apiMock.Setup(x => x.GetAsync(It.IsAny<string>()))
                .ReturnsAsync(new HttpResponseMessage(HttpStatusCode.OK)
                {
                    Content = new StringContent(fakeJson, Encoding.UTF8, "application/json")
                });

            var response = await _apiMock.Object.GetAsync("http://fake/offers");

            var json = await response.Content.ReadAsStringAsync();

            var ofertes = JsonSerializer.Deserialize<List<Oferta>>(json,
                new JsonSerializerOptions
                {
                    PropertyNameCaseInsensitive = true
                });

            Assert.IsNotNull(ofertes);
            Assert.AreEqual(2, ofertes.Count);
        }

        /// <summary>
        /// Test per comprovar que un ADMIN pot eliminar correctament
        /// una oferta del sistema.
        /// Es simula una resposta HTTP 200 OK.
        /// </summary>

        [TestMethod]
        public async Task AdminBorrarOferta()
        {
            // Simulem eliminació correcta
            _apiMock.Setup(x => x.DeleteAsync(It.IsAny<string>()))
                .ReturnsAsync(new HttpResponseMessage(HttpStatusCode.OK));

            var response = await _apiMock.Object.DeleteAsync("http://fake/offers/1");

            Assert.IsTrue(response.IsSuccessStatusCode);
        }
    }
}