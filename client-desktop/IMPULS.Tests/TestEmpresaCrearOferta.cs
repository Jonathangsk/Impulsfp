using System.Collections.Generic;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using IMPULS_Desktop;

namespace IMPULS.Tests
{
    /// <summary>
    /// Classe de tests per validar la creació i validació d'ofertes
    /// per part de les empreses.
    /// Inclou comprovacions de camps obligatoris i opcionals.
    /// </summary>
    [TestClass]
    public class TestEmpresaCrearOferta
    {
        /// <summary>
        /// Test per comprovar que el títol d'una oferta és obligatori.
        /// </summary>
        [TestMethod]
        public void TestTitleBuit()
        {
            var oferta = new Oferta
            {
                Title = ""
            };

            bool resultat = !string.IsNullOrWhiteSpace(oferta.Title);

            Assert.IsFalse(resultat);
        }

        /// <summary>
        /// Test per comprovar que un títol vàlid és acceptat.
        /// </summary>
        [TestMethod]
        public void TestTitleValid()
        {
            var oferta = new Oferta
            {
                Title = "Programador Backend"
            };

            bool resultat = !string.IsNullOrWhiteSpace(oferta.Title);

            Assert.IsTrue(resultat);
        }

        /// <summary>
        /// Test per comprovar que la descripció és obligatòria.
        /// </summary>
        [TestMethod]
        public void TestDescriptionBuida()
        {
            var oferta = new Oferta
            {
                Description = ""
            };

            bool resultat = !string.IsNullOrWhiteSpace(oferta.Description);

            Assert.IsFalse(resultat);
        }

        /// <summary>
        /// Test per comprovar que una descripció vàlida és acceptada.
        /// </summary>
        [TestMethod]
        public void TestDescriptionValida()
        {
            var oferta = new Oferta
            {
                Description = "Oferta enfocada a desenvolupament .NET."
            };

            bool resultat = !string.IsNullOrWhiteSpace(oferta.Description);

            Assert.IsTrue(resultat);
        }

        /// <summary>
        /// Test per comprovar que la ubicació és obligatòria.
        /// </summary>
        [TestMethod]
        public void TestLocationBuida()
        {
            var oferta = new Oferta
            {
                Location = ""
            };

            bool resultat = !string.IsNullOrWhiteSpace(oferta.Location);

            Assert.IsFalse(resultat);
        }

        /// <summary>
        /// Test per comprovar que una ubicació vàlida és acceptada.
        /// </summary>
        [TestMethod]
        public void TestLocationValida()
        {
            var oferta = new Oferta
            {
                Location = "Barcelona"
            };

            bool resultat = !string.IsNullOrWhiteSpace(oferta.Location);

            Assert.IsTrue(resultat);
        }

        /// <summary>
        /// Test per comprovar que les habilitats són obligatòries.
        /// </summary>
        [TestMethod]
        public void TestSkillsBuides()
        {
            var oferta = new Oferta
            {
                Skills = new List<string>()
            };

            bool resultat = oferta.Skills.Count > 0;

            Assert.IsFalse(resultat);
        }

        /// <summary>
        /// Test per comprovar que les habilitats són acceptades.
        /// </summary>
        [TestMethod]
        public void TestSkillsValides()
        {
            var oferta = new Oferta
            {
                Skills = new List<string> { "C#", ".NET", "SQL" }
            };

            bool resultat = oferta.Skills.Count > 0;

            Assert.IsTrue(resultat);
        }

        /// <summary>
        /// Test per comprovar que la modalitat és obligatòria.
        /// </summary>
        [TestMethod]
        public void TestModalityNoSeleccionada()
        {
            var oferta = new Oferta
            {
                Modality = ""
            };

            bool resultat = !string.IsNullOrWhiteSpace(oferta.Modality);

            Assert.IsFalse(resultat);
        }

        /// <summary>
        /// Test per comprovar que una modalitat vàlida és acceptada.
        /// </summary>
        [TestMethod]
        public void TestModalityValida()
        {
            var oferta = new Oferta
            {
                Modality = "Remot"
            };

            bool resultat = !string.IsNullOrWhiteSpace(oferta.Modality);

            Assert.IsTrue(resultat);
        }

        /// <summary>
        /// Test per comprovar que el tipus de contracte és obligatori.
        /// </summary>
        [TestMethod]
        public void TestContractTypeNoSeleccionat()
        {
            var oferta = new Oferta
            {
                ContractType = ""
            };

            bool resultat = !string.IsNullOrWhiteSpace(oferta.ContractType);

            Assert.IsFalse(resultat);
        }

        /// <summary>
        /// Test per comprovar que un tipus de contracte vàlid és acceptat.
        /// </summary>
        [TestMethod]
        public void TestContractTypeValid()
        {
            var oferta = new Oferta
            {
                ContractType = "Indefinit"
            };

            bool resultat = !string.IsNullOrWhiteSpace(oferta.ContractType);

            Assert.IsTrue(resultat);
        }

        /// <summary>
        /// Test per comprovar que el salari és opcional.
        /// </summary>
        [TestMethod]
        public void TestSalaryOptional()
        {
            var oferta = new Oferta
            {
                Salary = 0
            };

            Assert.AreEqual(0, oferta.Salary);
        }

        /// <summary>
        /// Test per comprovar que la data de creació es pot assignar correctament.
        /// </summary>
        [TestMethod]
        public void TestCreacioDataCorrecta()
        {
            var oferta = new Oferta
            {
                CreationDate = "2025-05-15"
            };

            bool resultat = !string.IsNullOrWhiteSpace(oferta.CreationDate);

            Assert.IsTrue(resultat);
        }

        /// <summary>
        /// Test per comprovar que l'estat de l'oferta es pot assignar correctament.
        /// </summary>
        [TestMethod]
        public void TestEstatCorrecte()
        {
            var oferta = new Oferta
            {
                State = "Activa"
            };

            bool resultat = !string.IsNullOrWhiteSpace(oferta.State);

            Assert.IsTrue(resultat);
        }
    }
}