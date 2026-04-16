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
    [TestClass]
    public class TestEmpresaConsultaImodificaPerfilEmpresa
    {
        [TestMethod]
        public async Task ConsultarPerfilEmpresa_RetornaEmpresa()
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
        [TestMethod]
        public async Task ModificarDireccio_Acceptar()
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
        [TestMethod]
        public async Task ModificarTelefon_Acceptar()
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
        [TestMethod]
        public async Task ModificarWebsite_Acceptar()
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
        [TestMethod]
        public async Task ModificarNiche_Acceptar()
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
        [TestMethod]
        public async Task ModificarTechnologies_AcceptarArray()
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