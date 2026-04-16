using System;
using System.Collections.Generic;

namespace IMPULS_Desktop
{
    public class Empresa
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Email { get; set; }
        public string Address { get; set; }
        public string VatNumber { get; set; }
        public string Website { get; set; }
        public string Phone { get; set; }
        public string Niche { get; set; }

        public List<string> Technologies { get; set; } = new List<string>();
        public string TechnologiesText { get; set; }

        public string Username { get; set; }
        public string Password { get; set; }

        // 🔥 CLAVE: acepta null y número
        public int? ActiveOffers { get; set; }
    }
}