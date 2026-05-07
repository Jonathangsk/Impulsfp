using System.Net;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;
using IMPULS_Desktop;
using IMPULS_Desktop.Services;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using Moq.Protected;

namespace IMPULS.Tests
{
    /// <summary>
    /// Tests del cambio de contraseña con mocks de HTTP y AlertService.
    /// </summary>
    [TestClass]
    public class TestCanviContrasenya
    {
        private Mock<HttpMessageHandler> handlerMock;
        private HttpClient httpClient;
        private Mock<IAlertService> alertMock;

        /// <summary>
        /// Inicialización de mocks antes de cada test.
        /// </summary>
        [TestInitialize]
        public void Setup()
        {
            handlerMock = new Mock<HttpMessageHandler>();
            httpClient = new HttpClient(handlerMock.Object);

            alertMock = new Mock<IAlertService>();
        }

        /// <summary>
        /// Si las contraseñas no coinciden, no debe llamarse a la API
        /// y debe mostrarse una alerta.
        /// </summary>
        [TestMethod]
        public async Task CanviContrasenyaIncorrecte()
        {
            var form = new CanviContrasenya("123", httpClient, alertMock.Object);

            form.TxtContrasenyaActual = "oldpass";
            form.TxtNovaContrasenya = "newpass1";
            form.TxtConfirmacio = "newpass2";

            await form.RestaurarAsync();

            handlerMock.Protected().Verify(
                "SendAsync",
                Times.Never(),
                ItExpr.IsAny<HttpRequestMessage>(),
                ItExpr.IsAny<CancellationToken>()
            );

            alertMock.Verify(
                x => x.Mostrar(
                    It.IsAny<string>(),
                    It.IsAny<string>(),
                    It.IsAny<string>()
                ),
                Times.Once()
            );
        }

        /// <summary>
        /// Caso correcto: se envía petición PATCH a la API.
        /// </summary>
        [TestMethod]
        public async Task CanviContrasenyaCorrecta()
        {
            HttpRequestMessage requestCapturada = null;

            handlerMock
                .Protected()
                .Setup<Task<HttpResponseMessage>>(
                    "SendAsync",
                    ItExpr.IsAny<HttpRequestMessage>(),
                    ItExpr.IsAny<CancellationToken>()
                )
                .Callback<HttpRequestMessage, CancellationToken>((req, ct) =>
                {
                    requestCapturada = req;
                })
                .ReturnsAsync(new HttpResponseMessage
                {
                    StatusCode = HttpStatusCode.OK,
                    Content = new StringContent("OK")
                });

            var form = new CanviContrasenya("123", httpClient, alertMock.Object);

            form.TxtContrasenyaActual = "oldpass";
            form.TxtNovaContrasenya = "newpass";
            form.TxtConfirmacio = "newpass";

            await form.RestaurarAsync();

            handlerMock.Protected().Verify(
                "SendAsync",
                Times.Once(),
                ItExpr.IsAny<HttpRequestMessage>(),
                ItExpr.IsAny<CancellationToken>()
            );

            Assert.IsNotNull(requestCapturada);

            Assert.AreEqual("PATCH", requestCapturada.Method.Method);

            string url = requestCapturada.RequestUri.ToString();
            Assert.IsTrue(url.Contains("sessionId=123"));
            Assert.IsTrue(url.Contains("/users/password"));

            var body = await requestCapturada.Content.ReadAsStringAsync();

            Assert.IsTrue(body.Contains("oldpass"));
            Assert.IsTrue(body.Contains("newpass"));
            Assert.IsTrue(body.Contains("currentPassword"));
            Assert.IsTrue(body.Contains("newPassword"));
        }
    }
}