using IMPULS_Desktop;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

namespace IMPULS.Tests
{
    /// <summary>
    /// Classe de tests per validar la funcionalitat d'autenticació d'usuaris.
    /// Conté tests per a usuaris de tipus ADMIN, COMPANY i usuaris incorrectes.
    /// </summary>
    
    [TestClass]
    public sealed class Test1
    {
        // Mètode auxiliar per simular la validació d'usuaris amb l'API
        // Retorna true i tipus d'usuari si les credencials són correctes, 
        // o false i null si són incorrectes o hi ha error.
        private async Task<(bool Success, string UserType)> ValidarUsuarioTest(string usuari, string contrasenya)
        {
            try
            {
                using (var client = new HttpClient())
                {
                    client.Timeout = TimeSpan.FromSeconds(10); // timeout máximo 10s
                    var apiBase = "http://localhost:8080/auth";
                    var url = $"{apiBase}/login";

                    // Preparem el JSON de la petició amb l'usuari i la contrasenya

                    var requestObj = new { username = usuari, password = contrasenya };
                    var json = JsonSerializer.Serialize(requestObj);
                    var content = new StringContent(json, Encoding.UTF8, "application/json");

                    var response = await client.PostAsync(url, content);

                    if (response.IsSuccessStatusCode)
                    {
                        // Si la resposta és correcta, deserialitzem el JSON de l'API

                        var responseString = await response.Content.ReadAsStringAsync();
                        var loginResponse = JsonSerializer.Deserialize<PantallaPrincipal.LoginResponse>(
                            responseString,
                            new JsonSerializerOptions { PropertyNameCaseInsensitive = true }
                        );

                        if (loginResponse != null && !string.IsNullOrWhiteSpace(loginResponse.UserType))
                        {
                            // Guardem SessionId i retornem el tipus d'usuari en majúscules
                           
                            string tipus = loginResponse.UserType.Trim().ToUpper();
                            PantallaPrincipal.SessionId = loginResponse.SessionId;
                            return (true, tipus);
                        }
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.Unauthorized)
                    {
                        // Usuari o contrasenya incorrectes
                        return (false, null);
                    }
                }
            }
            catch (Exception)
            {
                // Retorna false si hay error de conexión o timeout
                return (false, null);
            }

            return (false, null);
        }

        /// <summary>
        /// Test per validar un usuari de tipus ADMIN.
        /// Usuari: Jonathan
        /// Contrasenya: 1234
        /// S'espera que resultat sigui true i tipus sigui "ADMIN".
        /// </summary>
        [TestMethod] //Form2
        public async Task TestValidarUsuario_Admin()
        {
            var (resultat, tipus) = await ValidarUsuarioTest("Jonathan", "1234");

            Assert.IsTrue(resultat, "El usuario admin debería ser válido");
            Assert.AreEqual("ADMIN", tipus, "El tipo de usuario debería ser 'ADMIN'");
        }

        /// <summary>
        /// Test per validar un usuari de tipus COMPANY.
        /// Usuari: Josep
        /// Contrasenya: 1234
        /// S'espera que resultat sigui true i tipus sigui "COMPANY".
        /// </summary>
        [TestMethod] //Form4
        public async Task TestValidarUsuario_Empresa()
        {
            var (resultat, tipus) = await ValidarUsuarioTest("Josep", "1234");

            Assert.IsTrue(resultat, "El usuario empresa1 debería ser válido");
            Assert.AreEqual("COMPANY", tipus, "El tipo de usuario debería ser 'COMPANY'");
        }

        /// <summary>
        /// Test per validar un usuari incorrecte o no registrat.
        /// Usuari: alumneX
        /// Contrasenya: 0000
        /// S'espera que resultat sigui false i tipus sigui null.
        /// </summary>
        [TestMethod] //Form5
        public async Task TestValidarUsuario_Incorrecto()
        {
            var (resultat, tipus) = await ValidarUsuarioTest("alumneX", "0000");

            Assert.IsFalse(resultat, "El usuario incorrecto no debería validar");
            Assert.IsNull(tipus, "El tipo debería ser null cuando el usuario es incorrecto");
        }
    }
}