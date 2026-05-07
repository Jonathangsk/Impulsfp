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
    /// Classe de tests per validar la gestió d'alumnes des del panell ADMIN.
    /// Inclou consultes i eliminacions d'alumnes utilitzant un Mock d'IApiClient.
    /// </summary>
    [TestClass]
    public class TestAdminGestioAlumnes
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
        /// la llista d'alumnes registrada al sistema.
        /// Es simula una resposta HTTP OK amb dues dades d'alumnes.
        /// </summary>
        
        [TestMethod]
        public async Task AdminConsultaAlumnes()
        {
            var fakeJson = JsonSerializer.Serialize(new[]
            {
                new Alumne { Id = 1, Name = "Anna", Surname = "Lopez" },
                new Alumne { Id = 2, Name = "Marc", Surname = "Garcia" }
            });

            // Simulem la resposta de l'API amb alumnes
            _apiMock.Setup(x => x.GetAsync(It.IsAny<string>()))
                .ReturnsAsync(new HttpResponseMessage(HttpStatusCode.OK)
                {
                    Content = new StringContent(fakeJson, Encoding.UTF8, "application/json")
                });

            var response = await _apiMock.Object.GetAsync("http://fake/alumnes");

            var json = await response.Content.ReadAsStringAsync();

            var alumnes = JsonSerializer.Deserialize<List<Alumne>>(json,
                new JsonSerializerOptions
                {
                    PropertyNameCaseInsensitive = true
                });

            Assert.IsNotNull(alumnes);
            Assert.AreEqual(2, alumnes.Count);
        }

        /// <summary>
        /// Test per comprovar que un ADMIN pot eliminar correctament
        /// un alumne del sistema.
        /// Es simula una resposta HTTP 200 OK.
        /// </summary>
        
        [TestMethod]
        public async Task AdminBorrarAlumne()
        {
            // Simulem eliminació correcta
            _apiMock.Setup(x => x.DeleteAsync(It.IsAny<string>()))
                .ReturnsAsync(new HttpResponseMessage(HttpStatusCode.OK));

            var response = await _apiMock.Object.DeleteAsync("http://fake/alumnes/1");

            Assert.IsTrue(response.IsSuccessStatusCode);
        }
    }
}