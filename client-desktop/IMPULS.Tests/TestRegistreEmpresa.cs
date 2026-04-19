using IMPULS_Desktop;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Threading.Tasks;

namespace IMPULS.Tests
{
    /// <summary>
    /// Classe de tests per validar la funcionalitat d'autenticació d'usuaris i validacions de formulari.
    /// Inclou tests de login simulat, validació de username i validació de password.
    /// </summary>
    [TestClass]
    public sealed class TestRegistreEmpresa
    {
        /// <summary>
        /// Mètode fake utilitzat per simular la validació d'usuaris sense accés a API real.
        /// Retorna ADMIN, COMPANY o null segons les credencials.
        /// </summary>
        private Task<(bool Success, string UserType)> ValidarUsuariFake(string usuari, string contrasenya)
        {
            if (usuari == "Jonathan" && contrasenya == "1234")
                return Task.FromResult((true, "ADMIN"));

            if (usuari == "Josep" && contrasenya == "1234")
                return Task.FromResult((true, "COMPANY"));

            return Task.FromResult((false, (string)null));
        }

        /// <summary>
        /// Test per validar correctament un usuari de tipus ADMIN.
        /// </summary>
        [TestMethod]
        public async Task TestValidarUsuari_Admin()
        {
            var (resultat, tipus) = await ValidarUsuariFake("Jonathan", "1234");

            Assert.IsTrue(resultat);
            Assert.AreEqual("ADMIN", tipus);
        }

        /// <summary>
        /// Test per comprovar que el username és rebutjat si és massa curt.
        /// </summary>
        [TestMethod]
        public void Test_Username_Curt()
        {
            var form = new RegistreNovaEmpresa(null);

            bool resultat = form.ValidarUsername("ab");

            Assert.IsFalse(resultat);
        }

        /// <summary>
        /// Test per comprovar que el username és rebutjat si conté caràcters invàlids.
        /// </summary>
        [TestMethod]
        public void Test_Username_CaractersInvalids()
        {
            var form = new RegistreNovaEmpresa(null);

            bool resultat = form.ValidarUsername("empresa@123");

            Assert.IsFalse(resultat);
        }

        /// <summary>
        /// Test per comprovar que una contrasenya vàlida passa la validació.
        /// </summary>
        [TestMethod]
        public void Test_Password_Valida()
        {
            var form = new RegistreNovaEmpresa(null);

            bool resultat = form.ValidarPassword("Abc123");

            Assert.IsTrue(resultat);
        }

        /// <summary>
        /// Test per comprovar que una contrasenya massa curta és rebutjada.
        /// </summary>
        [TestMethod]
        public void Test_Password_Curta()
        {
            var form = new RegistreNovaEmpresa(null);

            bool resultat = form.ValidarPassword("A1b");

            Assert.IsFalse(resultat);
        }

        /// <summary>
        /// Test per comprovar que una contrasenya sense majúscules és rebutjada.
        /// </summary>
        [TestMethod]
        public void Test_Password_SenseMayuscula()
        {
            var form = new RegistreNovaEmpresa(null);

            bool resultat = form.ValidarPassword("abc123");

            Assert.IsFalse(resultat);
        }

        /// <summary>
        /// Test per comprovar que una contrasenya sense números és rebutjada.
        /// </summary>
        [TestMethod]
        public void Test_Password_SenseNumero()
        {
            var form = new RegistreNovaEmpresa(null);

            bool resultat = form.ValidarPassword("Abcdef");

            Assert.IsFalse(resultat);
        }

        /// <summary>
        /// Test per comprovar que un username vàlid és acceptat.
        /// </summary>
        [TestMethod]
        public void Test_Username_Valid()
        {
            var form = new RegistreNovaEmpresa(null);

            bool resultat = form.ValidarUsername("empresa123");

            Assert.IsTrue(resultat);
        }

        /// <summary>
        /// Test per validar un usuari de tipus COMPANY.
        /// </summary>
        [TestMethod]
        public async Task TestValidarUsuari_Empresa()
        {
            var (resultat, tipus) = await ValidarUsuariFake("Josep", "1234");

            Assert.IsTrue(resultat, "l'usuari empresa hauria de ser vàlid");
            Assert.AreEqual("COMPANY", tipus, "l'usuari hauria de ser 'COMPANY'");
        }

        /// <summary>
        /// Test per validar un usuari incorrecte o no registrat.
        /// </summary>
        [TestMethod]
        public async Task TestValidarUsuari_Incorrecte()
        {
            var (resultat, tipus) = await ValidarUsuariFake("alumneX", "0000");

            Assert.IsFalse(resultat, "l'usuari incorrecte no hauria de validar");
            Assert.IsNull(tipus, "El tipus hauria de ser null quan l'usuari és incorrecte");
        }
    }
}