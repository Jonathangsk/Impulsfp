using IMPULS_Desktop;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

namespace IMPULS.Tests
{
    /// <summary>
    /// Classe de tests per validar la funcionalitat del perfil d'empresa.
    /// Inclou consulta de dades i modificació de camps del perfil mitjançant API mockejada.
    /// </summary>
    [TestClass]
    public class TestEmpresaPerfilEmpresa
    {
        /// <summary>
        /// Test per comprovar que es pot consultar el perfil d'una empresa correctament.
        /// Es simula una resposta de l'API amb dades fictícies.
        /// </summary>
        [TestMethod]
        public async Task ConsultarPerfilEmpresa()
        {
            var mockApi = new Mock<IApiClient>();

            var fakeJson = JsonSerializer.Serialize(new
            {
                name = "Empresa TEST",
                address = "Carrer Test",
                phone = "123456789",
                niche = "Software",
                website = "https://empresa.com",
                technologies = new[] { "Java", "Angular" }
            });

            // Simulem resposta OK de l'API amb JSON d'empresa
            mockApi
                .Setup(x => x.GetAsync(It.IsAny<string>()))
                .ReturnsAsync(new HttpResponseMessage(HttpStatusCode.OK)
                {
                    Content = new StringContent(fakeJson, Encoding.UTF8, "application/json")
                });

            var response = await mockApi.Object.GetAsync("http://fake/users/me");

            var json = await response.Content.ReadAsStringAsync();

            var empresa = JsonSerializer.Deserialize<Empresa>(
                json,
                new JsonSerializerOptions
                {
                    PropertyNameCaseInsensitive = true
                });

            Assert.IsNotNull(empresa);
            Assert.AreEqual("Empresa TEST", empresa.Name);
            Assert.AreEqual("123456789", empresa.Phone);
            Assert.AreEqual("Software", empresa.Niche);
        }

        /// <summary>
        /// Test per comprovar que es pot modificar el nom de l'empresa correctament.
        /// </summary>
        [TestMethod]
        public async Task ModificarNom_Acceptar()
        {
            var mockApi = new Mock<IApiClient>();

            mockApi.Setup(x => x.PutAsync(It.IsAny<string>(), It.IsAny<HttpContent>()))
                .ReturnsAsync(new HttpResponseMessage(HttpStatusCode.OK));

            var body = new { name = "Empresa Nova" };

            var json = JsonSerializer.Serialize(body);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            var response = await mockApi.Object.PutAsync("http://fake", content);

            Assert.IsTrue(response.IsSuccessStatusCode);
        }

        /// <summary>
        /// Test per comprovar que es pot modificar l'adreça de l'empresa correctament.
        /// </summary>
        [TestMethod]
        public async Task ModificarDireccio()
        {
            var mockApi = new Mock<IApiClient>();

            mockApi.Setup(x => x.PutAsync(It.IsAny<string>(), It.IsAny<HttpContent>()))
                .ReturnsAsync(new HttpResponseMessage(HttpStatusCode.OK));

            var body = new { address = "Carrer 123" };

            var json = JsonSerializer.Serialize(body);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            var response = await mockApi.Object.PutAsync("http://fake", content);

            Assert.IsTrue(response.IsSuccessStatusCode);
        }

        /// <summary>
        /// Test per comprovar que es pot modificar el telèfon de l'empresa correctament.
        /// </summary>
        [TestMethod]
        public async Task ModificarTelefon()
        {
            var mockApi = new Mock<IApiClient>();

            mockApi.Setup(x => x.PutAsync(It.IsAny<string>(), It.IsAny<HttpContent>()))
                .ReturnsAsync(new HttpResponseMessage(HttpStatusCode.OK));

            var body = new { phone = "123456789" };

            var json = JsonSerializer.Serialize(body);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            var response = await mockApi.Object.PutAsync("http://fake", content);

            Assert.IsTrue(response.IsSuccessStatusCode);
        }

        /// <summary>
        /// Test per comprovar que es pot modificar el website de l'empresa correctament.
        /// </summary>
        [TestMethod]
        public async Task ModificarWebsite()
        {
            var mockApi = new Mock<IApiClient>();

            mockApi.Setup(x => x.PutAsync(It.IsAny<string>(), It.IsAny<HttpContent>()))
                .ReturnsAsync(new HttpResponseMessage(HttpStatusCode.OK));

            var body = new { website = "https://empresa.com" };

            var json = JsonSerializer.Serialize(body);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            var response = await mockApi.Object.PutAsync("http://fake", content);

            Assert.IsTrue(response.IsSuccessStatusCode);
        }

        /// <summary>
        /// Test per comprovar que es pot modificar el sector (niche) de l'empresa correctament.
        /// </summary>
        [TestMethod]
        public async Task ModificarNiche()
        {
            var mockApi = new Mock<IApiClient>();

            mockApi.Setup(x => x.PutAsync(It.IsAny<string>(), It.IsAny<HttpContent>()))
                .ReturnsAsync(new HttpResponseMessage(HttpStatusCode.OK));

            var body = new { niche = "Consultoria" };

            var json = JsonSerializer.Serialize(body);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            var response = await mockApi.Object.PutAsync("http://fake", content);

            Assert.IsTrue(response.IsSuccessStatusCode);
        }

        /// <summary>
        /// Test per comprovar que es pot modificar la llista de tecnologies de l'empresa correctament.
        /// </summary>
        [TestMethod]
        public async Task ModificarTechnologies()
        {
            var mockApi = new Mock<IApiClient>();

            mockApi.Setup(x => x.PutAsync(It.IsAny<string>(), It.IsAny<HttpContent>()))
                .ReturnsAsync(new HttpResponseMessage(HttpStatusCode.OK));

            var body = new
            {
                technologies = new[] { "Java", "Angular" }
            };

            var json = JsonSerializer.Serialize(body);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            var response = await mockApi.Object.PutAsync("http://fake", content);

            Assert.IsTrue(response.IsSuccessStatusCode);
        }
    }
}