using System;
using System.Collections.Generic;

namespace IMPULS_Desktop
{
    /// <summary>
    /// Representa una oferta laboral dins del sistema IMPULS.
    /// Conté informació sobre el lloc de treball i les seves condicions.
    /// </summary>
    public class Oferta
    {
        public int Id { get; set; }

        public string Title { get; set; }
        public string Description { get; set; }

        public string CompanyName { get; set; }

        public string Location { get; set; }
        public List<string> Skills { get; set; } = new List<string>();

        public string Modality { get; set; }

        public string ContractType { get; set; }

        public decimal Salary { get; set; }
        public string CreationDate { get; set; }

        public string State { get; set; }
   
        public int ApplicantsCount { get; set; }
    }
}