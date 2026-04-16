using IMPULS_Desktop;
using Microsoft.VisualStudio.TestTools.UnitTesting;
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
        // Metode fake, nomes per test
        private Task<(bool Success, string UserType)> ValidarUsuariFake(string usuari, string contrasenya)
        {
            if (usuari == "Jonathan" && contrasenya == "1234")
                return Task.FromResult((true, "ADMIN"));
            if (usuari == "Josep" && contrasenya == "1234")
                return Task.FromResult((true, "COMPANY"));
            return Task.FromResult((false, (string)null));
        }

        
        [TestMethod]
        public async Task TestValidarUsuari_Admin()
        {
            var (resultat, tipus) = await ValidarUsuariFake("Jonathan", "1234");

            Assert.IsTrue(resultat);
            Assert.AreEqual("ADMIN", tipus);
        }
        [TestMethod]
        public void Test_Username_Curt()
        {
            var form = new RegistreNovaEmpresa(null);

            bool resultat = form.ValidarUsername("ab");

            Assert.IsFalse(resultat);
        }
        [TestMethod]
        public void Test_Username_CaractersInvalids()
        {
            var form = new RegistreNovaEmpresa(null);

            bool resultat = form.ValidarUsername("empresa@123");

            Assert.IsFalse(resultat);
        }
        [TestMethod]
        public void Test_Password_Valida()
        {
            var form = new RegistreNovaEmpresa(null);

            bool resultat = form.ValidarPassword("Abc123");

            Assert.IsTrue(resultat);
        }
        [TestMethod]
        public void Test_Password_Curta()
        {
            var form = new RegistreNovaEmpresa(null);

            bool resultat = form.ValidarPassword("A1b");

            Assert.IsFalse(resultat);
        }

        [TestMethod]
        public void Test_Password_SenseMayuscula()
        {
            var form = new RegistreNovaEmpresa(null);

            bool resultat = form.ValidarPassword("abc123");

            Assert.IsFalse(resultat);
        }
        [TestMethod]
        public void Test_Password_SenseNumero()
        {
            var form = new RegistreNovaEmpresa(null);

            bool resultat = form.ValidarPassword("Abcdef");

            Assert.IsFalse(resultat);
        }

        [TestMethod]
        public void Test_Username_Valid()
        {
            var form = new RegistreNovaEmpresa(null);

            bool resultat = form.ValidarUsername("empresa123");

            Assert.IsTrue(resultat);
        }

        /// <summary>
        /// Test per validar un usuari de tipus COMPANY.
        /// Usuari: Josep
        /// Contrasenya: 1234
        /// S'espera que resultat sigui true i tipus sigui "COMPANY".
        /// </summary>
        [TestMethod] //Form4
        public async Task TestValidarUsuari_Empresa()
        {
           
            var (resultat, tipus) = await ValidarUsuariFake("Josep", "1234");
            Assert.IsTrue(resultat, "l'usuari empresa1 hauria de ser válid");
            Assert.AreEqual("COMPANY", tipus, "l'usuari hauria de ser 'COMPANY'");
        }

        /// <summary>
        /// Test per validar un usuari incorrecte o no registrat.
        /// Usuari: alumneX
        /// Contrasenya: 0000
        /// S'espera que resultat sigui false i tipus sigui null.
        /// </summary>
        [TestMethod] //Form5
        public async Task TestValidarUsuari_Incorrecte()
        {
            var (resultat, tipus) = await ValidarUsuariFake("alumneX", "0000");

            Assert.IsFalse(resultat, "l'usuari incorrecte no hauria de validar");
            Assert.IsNull(tipus, "El tipus hauria de ser null quen l'usuari es incorrecte");
        }
    }
}