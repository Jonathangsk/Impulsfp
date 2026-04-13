using System;
using System.Collections.Generic;

namespace IMPULS_Desktop
{
    internal class Oferta
    {
        public int Id { get; set; }

        public string Title { get; set; }
        public string Description { get; set; }

        public Empresa Company { get; set; } = new Empresa();

        public string RequiredSkills { get; set; }

        public string Location { get; set; }

        public string Modality { get; set; } // remote / hybrid / presencial

        public string ContractType { get; set; } // prácticas / junior / senior

        public decimal Salary { get; set; }

        public DateTime CreationDate { get; set; }

        public string Cicle { get; set; }

        public string Estat { get; set; }

        public string Observacions { get; set; }

        public List<string> Applicants { get; set; } = new List<string>();
    }
}