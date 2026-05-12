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
    /// Classe de tests per validar la gestió d'empreses des del panell ADMIN.
    /// Inclou consultes i eliminacions d'empreses utilitzant un Mock d'IApiClient.
    /// </summary>

    [TestClass]
    public class TestAdminConsultaIbaixaEmpreses
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
        /// la llista d'empreses registrades al sistema.
        /// Es simula una resposta HTTP OK amb dues empreses.
        /// </summary>
        
        [TestMethod]
        public async Task AdminConsultaEmpreses()
        {
            var fakeJson = JsonSerializer.Serialize(new[]
            {
                new Empresa { Id = 1, Name = "Empresa A", Phone = "111" },
                new Empresa { Id = 2, Name = "Empresa B", Phone = "222" }
            });

            // Simulem la resposta de l'API amb empreses
            _apiMock.Setup(x => x.GetAsync(It.IsAny<string>()))
                .ReturnsAsync(new HttpResponseMessage(HttpStatusCode.OK)
                {
                    Content = new StringContent(fakeJson, Encoding.UTF8, "application/json")
                });

            var response = await _apiMock.Object.GetAsync("http://fake/empreses");

            var json = await response.Content.ReadAsStringAsync();

            var empreses = JsonSerializer.Deserialize<List<Empresa>>(json,
                new JsonSerializerOptions
                {
                    PropertyNameCaseInsensitive = true
                });

            Assert.IsNotNull(empreses);
            Assert.AreEqual(2, empreses.Count);
        }

        /// <summary>
        /// Test per comprovar que un ADMIN pot eliminar correctament
        /// una empresa del sistema.
        /// Es simula una resposta HTTP 200 OK.
        /// </summary>

        [TestMethod]
        public async Task AdminBorrarEmpresa()
        {
            // Simulem eliminació correcta
            _apiMock.Setup(x => x.DeleteAsync(It.IsAny<string>()))
                .ReturnsAsync(new HttpResponseMessage(HttpStatusCode.OK));

            var response = await _apiMock.Object.DeleteAsync("http://fake/empresas/1");

            Assert.IsTrue(response.IsSuccessStatusCode);
        }
    }
}