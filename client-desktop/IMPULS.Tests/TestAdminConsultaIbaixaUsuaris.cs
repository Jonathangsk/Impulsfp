using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using IMPULS_Desktop;
using Microsoft.VisualBasic.ApplicationServices;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;

namespace IMPULS.Tests

{
    [TestClass]
    public class TestApiAdmin
    {
        private Mock<IApiClient> _apiMock;

        [TestInitialize]
        public void Setup()
        {
            _apiMock = new Mock<IApiClient>();
        }

        [TestMethod]
    public async Task Admin_ConsultaAlumnes()
    {
        var fakeJson = JsonSerializer.Serialize(new[]
        {
        new Alumne { Id = 1, Name = "Anna", Surname = "Lopez" },
        new Alumne { Id = 2, Name = "Marc", Surname = "Garcia" }
    });

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
        Assert.AreEqual("Anna", alumnes[0].Name);
    }

        [TestMethod]
        public async Task Admin_ConsultaEmpreses()
        {
            var fakeJson = JsonSerializer.Serialize(new[]
            {
        new Empresa { Id = 1, Name = "Empresa A", Phone = "111" },
        new Empresa { Id = 2, Name = "Empresa B", Phone = "222" }
    });

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
            Assert.AreEqual("Empresa A", empreses[0].Name);
        }

        [TestMethod]
        public async Task Admin_BorrarAlumne()
        {
            _apiMock.Setup(x => x.DeleteAsync(It.IsAny<string>()))
                .ReturnsAsync(new HttpResponseMessage(HttpStatusCode.OK));

            var response = await _apiMock.Object.DeleteAsync("http://fake/alumnes/1");

            Assert.IsTrue(response.IsSuccessStatusCode);
        }

        [TestMethod]
        public async Task Admin_BorrarEmpresa()
        {
            _apiMock.Setup(x => x.DeleteAsync(It.IsAny<string>()))
                .ReturnsAsync(new HttpResponseMessage(HttpStatusCode.OK));

            var response = await _apiMock.Object.DeleteAsync("http://fake/empresas/1");

            Assert.IsTrue(response.IsSuccessStatusCode);
        }
    }
}